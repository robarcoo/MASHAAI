package com.example.data

import com.example.domain.datasource.LocalDataSource
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import kotlinx.coroutines.flow.Flow


class LocalChatService(private val messageDao: MessageDao) : LocalDataSource {

    override fun addChat(chat : ChatInfo) {
        messageDao.insertChat(chat)
    }

    override fun getChat(id : Int): Flow<ChatInfo> {
        return messageDao.getChatInfo(id)
    }

    override fun getChatList(): Flow<List<ChatInfo>> {
        return messageDao.getChatInfoList()
    }

    override fun addMessage(message: Message) : Long {
        return messageDao.insertMessage(message)
    }
}