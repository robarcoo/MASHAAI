package com.example.mashaai.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MessageRepository
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import com.example.domain.models.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: MessageRepository) : ViewModel() {

    private var _chatListState = MutableStateFlow<List<ChatInfo>>(emptyList())
    val chatListState : StateFlow<List<ChatInfo>> = _chatListState.asStateFlow()
    private var _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            repository
                .getChatList()
                .stateIn(scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    Result.Loading(Unit)
                ).collect {
                    when (it) {
                        is Result.Success<*> -> {
                            val finalData = it.value as List<*>
                            _chatListState.update {
                                finalData.filterIsInstance<ChatInfo>()
                            }
                            _isError.update { false }
                            _isLoading.update { false }
                        }
                        is Result.Error -> {
                            _isError.update { true }
                        }
                        is Result.Loading -> {
                            _isLoading.update { true }
                        }
                    }
                }
        }
        }


    private fun saveMessage(chatInfo: ChatInfo, message : String, isBot : Boolean) {
        repository.createMessage(Message(id = chatInfo.messageList.maxBy { it.id }.id + 1, chatId = chatInfo.id, message = message, isBotAnswer = isBot))
    }

    fun createChat(chatInfo: ChatInfo) {
        repository.createChat(chatInfo)
    }

    fun sendMessage(chatInfo: ChatInfo, message: String) {
        saveMessage(chatInfo, message, false)
        viewModelScope.launch {
            repository
                .sendQuestion(message)
                .stateIn(scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    Result.Loading(Unit)
                ).collect {
                    when (it) {
                        is Result.Success<*> -> {
                            val finalData = it.value as String
                            saveMessage(chatInfo, finalData, true)
                            _chatListState.update { chatList ->
                                chatList.apply {
                                    chatList[chatList.indexOf(chatInfo)].messageList.add(
                                        Message(
                                            chatId = chatInfo.id,
                                            message = finalData,
                                            isBotAnswer = true
                                        )
                                    )
                                }
                            }
                            _isError.update { false }
                            _isLoading.update { false }
                        }
                        is Result.Error -> {
                            _isError.update { true }
                        }
                        is Result.Loading -> {
                            _isLoading.update { true }
                        }
                    }
                }
        }
    }
}