package com.example.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity
data class ChatInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val image: Int? = null,
    val name : String,
    @ColumnInfo(name="message_list")
    var messageList: List<Message> = emptyList()
)


