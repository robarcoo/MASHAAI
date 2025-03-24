package com.example.data

import com.example.domain.datasource.RemoteDataSource
import com.example.domain.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI


class MessageService(private val client : HttpClient) : RemoteDataSource<Message> {

    @OptIn(InternalAPI::class)
    override suspend fun sendQuestion(message: String): HttpResponse {
        return client.post {
            contentType(ContentType.Application.Json)
            body = """{"text": "$message"}"""
        }
    }

}