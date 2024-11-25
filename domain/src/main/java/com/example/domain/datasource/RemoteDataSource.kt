package com.example.domain.datasource

import io.ktor.client.statement.HttpResponse

interface RemoteDataSource<T> {
    suspend fun sendQuestion(message : String) : HttpResponse
}