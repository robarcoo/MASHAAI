package com.example.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "chat_id")
    var chatId : Int,
    val message: String,
    @ColumnInfo(name = "is_bot_answer")
    val isBotAnswer : Boolean = true
)