package com.example.domain.models

data class ChatInfo(
    val image: Int? = null,
    val name : String,
    val messageList: List<Message>
)

data class Message(
    val message: String,
    val isBotAnswer : Boolean = true,
    val isRead : Boolean = true
)