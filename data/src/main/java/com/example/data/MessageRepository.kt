package com.example.data

import com.example.domain.models.Message
import kotlinx.coroutines.flow.flow



class MessageRepository(private val socketClient: KtorWebsocketClient)  {

    fun connect() = socketClient.connect()

    suspend fun sendMessage(message : Message) = socketClient.send(message = message)

    suspend fun disconnect() = socketClient.stop()
}