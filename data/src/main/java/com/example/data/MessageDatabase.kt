package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import androidx.room.Upsert
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import kotlinx.coroutines.flow.Flow

@Database(entities = [ChatInfo::class,
    Message::class], version = 5)
@TypeConverters(MessageConverter::class)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao() : MessageDao
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM ChatInfo")
    fun getChatInfoList() : Flow<List<ChatInfo>>
    @Query("SELECT * FROM ChatInfo WHERE ChatInfo.id == (:id)")
    fun getChatInfo(id: Int) : Flow<ChatInfo>
    @Insert(entity = ChatInfo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat : ChatInfo)
    @Update(entity = ChatInfo::class)
    fun updateChat(chat: ChatInfo)
}