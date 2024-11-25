package com.example.data

import com.example.domain.datasource.RemoteDataSource
import com.example.domain.models.DataAnswer
import com.example.domain.models.Message
import com.example.domain.models.Result
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException




class MessageRepository(private val remoteDataSource: RemoteDataSource<Message>)  {

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
}