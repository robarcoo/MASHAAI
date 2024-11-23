package com.example.data

import kotlinx.coroutines.flow.flow



class MessageRepository(private val socketClient: KtorWebsocketClient)  {

    fun connect() = socketClient.connect()

    suspend fun sendMessage(message : String) = socketClient.send(message = message)

    suspend fun disconnect() = socketClient.stop()
}