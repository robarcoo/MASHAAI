package com.example.data

import androidx.room.TypeConverter
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageConverter {
    @TypeConverter
    fun fromMessageObject(message: Message?): String {
        val type = object : TypeToken<Message?>() {}.type
        return Gson().toJson(message, type)
    }

    @TypeConverter
    fun toMessageObject(message: String?) : Message? {
        val type = object : TypeToken<Message?>() {}.type
        return Gson().fromJson(message, type)
    }

    @TypeConverter
    fun fromChatInfoObject(chat: ChatInfo?): String {
        val type = object : TypeToken<ChatInfo?>() {}.type
        return Gson().toJson(chat, type)
    }

    @TypeConverter
    fun toChatInfoObject(chat: String?) : ChatInfo? {
        val type = object : TypeToken<ChatInfo?>() {}.type
        return Gson().fromJson(chat, type)
    }

    @TypeConverter
    fun fromMessageList(messages: MutableList<Message>?): String? {
        val type = object : TypeToken<MutableList<Message>?>() {}.type
        return Gson().toJson(messages, type)
    }

    @TypeConverter
    fun toMessageList(messageListString: String?): MutableList<Message>? {
        val type = object : TypeToken<MutableList<Message>?>() {}.type
        return Gson().fromJson(messageListString, type)
    }
}