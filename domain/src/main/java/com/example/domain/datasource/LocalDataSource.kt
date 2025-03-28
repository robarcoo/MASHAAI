package com.example.domain.datasource

import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getChatList() : Flow<List<ChatInfo>>
    fun getChat(id : Int) : Flow<ChatInfo>
    fun updateChat(chat : ChatInfo)
    fun addChat(chat: ChatInfo)
}

