package com.example.mashaai.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MessageRepository
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import com.example.domain.models.Result
import kotlinx.coroutines.Dispatchers
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
                ).collect { result ->
                    val finalData = result as? List<*>
                    if (finalData != null) {
                        _chatListState.update {
                            finalData.filterIsInstance<ChatInfo>()
                        }
                    }
                }
        }
        }


    private fun saveMessage(chatInfo: ChatInfo, message : String, isBot : Boolean){
        Log.d("MASHA", message)
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repository.createMessage(Message(
                id = 0,
                chatId = chatInfo.id,
                message = message,
                isBotAnswer = isBot
            )
            ).toInt()

            val temp = _chatListState.value.toMutableList()
            Log.d("MASHA", temp.toString())
            temp.map { if (it.id == chatInfo.id) {
                it.messageList += Message(newId, chatInfo.id, message, isBot)
            } }
            Log.d("MASHA", temp.toString())
            _chatListState.update {
                temp
            }
        }
        }

    fun createChat(chatInfo: ChatInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createChat(chatInfo)
            val temp = _chatListState.value.toMutableList()
            temp.add(chatInfo)
            _chatListState.update {
                temp
            }
        }
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
                            _isError.update { false }
                            _isLoading.update { false }
                        }
                        is Result.Error -> {
                            _isError.update { true }
                        }
                        is Result.Loading -> {
                            _isLoading.update { true }
                        }

                        else -> {}
                    }
                }
        }
    }
}