package com.example.data

import com.example.domain.datasource.RemoteDataSource
import com.example.domain.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse



class MessageService(private val client : HttpClient) : RemoteDataSource<Message> {

    private val SEND_QUESTION = "get"

    override suspend fun sendQuestion(message: String): HttpResponse {
        return client.get("$SEND_QUESTION?question=$message")
    }

}