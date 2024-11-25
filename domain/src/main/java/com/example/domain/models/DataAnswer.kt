package com.example.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataAnswer<T>(
    @SerialName("status")
    val status: Boolean = true,
    @SerialName("code")
    val code: String = "200",
    @SerialName("message")
    val message: String = "OK",
    @SerialName("time")
    val time: String = "",
    @SerialName("data")
    val data : List<T>
)