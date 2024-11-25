package com.example.domain.models

sealed class Result{
    data class Success<T>(val value: T): Result()

    data class Error(val value: Throwable): Result()//custom err

    data class Loading(val value: Unit) : Result()
}
