package com.example.mashaai.di

import android.content.Context
import androidx.room.Room
import com.example.data.LocalChatService
import com.example.data.MessageDao
import com.example.data.MessageDatabase
import com.example.data.MessageRepository
import com.example.data.MessageService
import com.example.data.client
import com.example.domain.datasource.LocalDataSource
import com.example.domain.datasource.RemoteDataSource
import com.example.domain.models.Message
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        provideHttpClient()
    }

    single {
        provideDB(context = androidContext())
    }

    single {
        provideDao(database = get())
    }

    single<MessageRepository> {
        MessageRepository(remoteDataSource = get(), localChatService = get())
    }

    single<RemoteDataSource<Message>> {
        MessageService(client = get())
    }

    single<LocalDataSource> {
        LocalChatService(messageDao = get())
    }
}

fun provideHttpClient(): HttpClient {
    return client
}

fun provideDB(context : Context) : MessageDatabase {
    return Room.databaseBuilder(context, MessageDatabase::class.java, "database.db")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideDao(database: MessageDatabase) : MessageDao {
    return database.messageDao()
}


