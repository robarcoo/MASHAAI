package com.example.mashaai.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MessageRepository
import com.example.domain.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: MessageRepository) : ViewModel() {

    var messages = mutableListOf<Message>()
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting = _isConnecting.asStateFlow()
    val state = repository.connect()
        .onStart { _isConnecting.value = true }
        .onEach { _isConnecting.value = false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), String())


    fun sendMessage(message : String) {
        viewModelScope.launch {
            messages.add(Message(message, false))
            repository.sendMessage(Message(message, false))
        }
    }


    init {
        viewModelScope.launch {
            repository.connect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            repository.disconnect()
        }
    }
}