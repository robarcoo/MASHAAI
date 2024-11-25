package com.example.mashaai.di

import com.example.mashaai.viewmodels.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel<ChatViewModel> {
        ChatViewModel(get())
    }
}
