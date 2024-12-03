package com.example.data

import com.example.domain.datasource.RemoteDataSource
import com.example.domain.models.ChatInfo
import com.example.domain.models.DataAnswer
import com.example.domain.models.Message
import com.example.domain.models.Result
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException




class MessageRepository(private val remoteDataSource: RemoteDataSource<Message>,
                        private val localChatService: LocalChatService)  {

    fun sendQuestion(message : String) : Flow<Result> {
        return flow {
            val response = remoteDataSource.sendQuestion(message)
            when (response.status) {
                HttpStatusCode.OK -> {
                    try {
                        emit(Result.Success(value = response.body<DataAnswer<Message>>()))
                    } catch (e: SerializationException) {
                        emit(
                            Result.Error(value = Exception("error"))
                        )
                    }
                } else -> {
                emit(Result.Error(value = Exception("error")))
                }
            }
        }
    }

    fun getChatList() : Flow<List<ChatInfo>> {
        return localChatService.getChatList()

    }

    fun getChat(id : Int) : Flow<ChatInfo> {
        return localChatService.getChat(id)
    }

    fun createChat(chatInfo: ChatInfo) {
        localChatService.addChat(chatInfo)
    }

    fun createMessage(message: Message) {
        localChatService.addMessage(message)
    }
}