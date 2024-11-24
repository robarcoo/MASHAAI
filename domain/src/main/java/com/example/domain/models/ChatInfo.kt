package com.example.domain.models

import kotlinx.serialization.Serializable

data class ChatInfo(
    val image: Int? = null,
    val name : String,
    val messageList: List<Message>
)

@Serializable
data class Message(
    val message: String,
    val isBotAnswer : Boolean = true,
    val isRead : Boolean = true
)