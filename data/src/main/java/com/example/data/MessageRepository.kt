package com.example.data

import android.util.Log
import com.example.domain.datasource.RemoteDataSource
import com.example.domain.models.ChatInfo
import com.example.domain.models.DataAnswer
import com.example.domain.models.Message
import com.example.domain.models.Result
import com.google.gson.Gson
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@Serializable
data class ServerResponse(
    val text : String
)


class MessageRepository(private val remoteDataSource: RemoteDataSource<Message>,
                        private val localChatService: LocalChatService)  {

    fun sendQuestion(message : String) : Flow<Result> {
        return flow {
            try {
                val response = remoteDataSource.sendQuestion(message)
                Log.d("MASHSHSHA", response.body<String>())
                when (response.status) {
                    HttpStatusCode.OK -> {
                        emit(Result.Success(value = response.body<ServerResponse>()))

                    }

                    else -> {
                        emit(Result.Error(value = Exception("error")))
                    }
                }
            } catch (e: Exception) {
                Log.d("MASHSHSHA", e.toString())
                emit(
                    Result.Error(value = Exception("error"))
                )

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

    fun updateChat(chatInfo: ChatInfo) {
        localChatService.updateChat(chatInfo)
    }
}