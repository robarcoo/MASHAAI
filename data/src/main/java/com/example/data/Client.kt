package com.example.data

import android.util.Log
import com.example.domain.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit




class KtorWebsocketClient {

private val client = HttpClient(OkHttp) {
    engine {
        config {
            pingInterval(PING_INTERVAL, TimeUnit.MILLISECONDS)
        }
    }
    install(ContentNegotiation) {
        json()
    }

    install(Logging)
    install(WebSockets)
}
    companion object {
        private const val url = "wss://echo.websocket.org/.sse"
        private const val RECONNECT_DELAY = 10_000L
        private const val PING_INTERVAL = 5_000L
        private const val TAG = "EVChargingWebSocketClient"
    }



    private val scope = CoroutineScope(Dispatchers.IO) + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        Log.d(
            TAG,
            "Error: ${throwable.message}",
        )
    }

    private var job: Job? = null

    private var session: WebSocketSession? = null

    fun connect() : Flow<String> {
        return flow {
            try {
                Log.d(
                    TAG,
                    "Connecting to websocket at $url..."
                )

                session = client.webSocketSession {
                    url("wss://echo.websocket.org")
                }


                Log.d(
                    TAG,
                    "Connected to websocket at $url"
                )

                val messageStates = session!!
                    .incoming
                    .consumeAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .mapNotNull {
                        it.readText()
                    }
                Log.d(
                    TAG,
                    "Answer: $messageStates"
                )
                emitAll(messageStates)
            } catch (e: Exception) {

                Log.d(
                    TAG,
                    "Error: ${e.message}",
                )

                reconnect()
            }
        }
    }

    private fun reconnect() {
        job?.cancel()

        Log.d(
            TAG,
            "Reconnecting to websocket in ${RECONNECT_DELAY}ms..."
        )

        job = scope.launch {
            stop()
            delay(RECONNECT_DELAY)
            connect()
        }
    }

    suspend fun stop() {
        Log.d(
            TAG,
            "Closing websocket session..."
        )

        session?.close()
        session = null
    }

    suspend fun send(message: Message) {
        Log.d(
            TAG,
            "Sending message: $message"
        )

        session?.outgoing?.send(
            Frame.Text(Json.encodeToString(message))
        )
    }

}