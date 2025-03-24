package com.example.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.http
import kotlinx.serialization.json.Json
import java.net.InetSocketAddress
import java.net.Proxy


private const val NETWORK_TIME_OUT = Long.MAX_VALUE

val client =  HttpClient(Android) {
    install(ContentNegotiation) {
        register(ContentType.Text.Html, KotlinxSerializationConverter(
            Json {
                json()
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }
        ))

    }
    install(DefaultRequest) {
        url("http://85.143.167.11:8000/ask")
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = NETWORK_TIME_OUT
        connectTimeoutMillis = NETWORK_TIME_OUT
        socketTimeoutMillis = NETWORK_TIME_OUT
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    install(ResponseObserver) {
        onResponse { response ->
            Log.d("HTTP status:", "${response.status.value}")
        }
    }


}