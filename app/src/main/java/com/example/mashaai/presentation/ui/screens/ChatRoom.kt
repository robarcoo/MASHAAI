package com.example.mashaai.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.KtorWebsocketClient
import com.example.data.MessageRepository
import com.example.domain.models.Message
import com.example.mashaai.R
import com.example.mashaai.viewmodels.ChatViewModel
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChatRoomHeader() {
    val viewModel = ChatViewModel(repository = MessageRepository(socketClient = KtorWebsocketClient()))
    val text = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Name") },
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                navigationIcon = { Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Вернуться к списку диалогов") },
                actions = { Icon(painter = painterResource(id = R.drawable.ic_default_avatar), contentDescription = "Аватар чат-бота")})

        },
        bottomBar = {
            Box(contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.padding(16.dp)) {
                BasicTextField(
                    value = text.value, onValueChange = { text.value = it },
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .clip(RoundedCornerShape(71.dp))
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .height(40.dp),
                ) {
                    TextFieldDefaults.DecorationBox(
                        value = text.value,
                        innerTextField = it,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        trailingIcon = { IconButton(onClick = { viewModel.sendMessage(text.value) }) {
                            Icon(
                                Icons.AutoMirrored.Default.Send,
                                contentDescription = "Отправить сообщение"
                            )
                        }
                        },
                        placeholder = { Text("Напиши, что хочешь") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Gray,
                            unfocusedContainerColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
                    )
                }

            }
            
        }

    ) { innerPadding ->
        ChatRoomScreen(innerPadding, viewModel)
    }
}



@Composable
fun ChatRoomScreen(innerPadding: PaddingValues, viewModel: ChatViewModel) {
    val state by viewModel.state.collectAsState()

    runCatching {
        Json.decodeFromString<Message>(state)
    }.onSuccess {
        viewModel.messages += Message(it.message, isRead = false)
    }


    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(items = viewModel.messages) { message ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 6.dp,
                    start = if (message.isBotAnswer) {
                        16.dp
                    } else {
                        36.dp
                    },
                    end = if (message.isBotAnswer) {
                        36.dp
                    } else {
                        16.dp
                    },
                    bottom = 6.dp
                ),
            horizontalArrangement =
            if (message.isBotAnswer)
                Arrangement.Start
            else
                Arrangement.End
            ) {
            MessageBox(
                text = message.message,
                isBot = message.isBotAnswer)
            }
        }
    }
}

@Composable
fun MessageBox(text: String, isBot: Boolean) {
    val backgroundColor = if (isBot) { Color.Gray } else { Color.Blue }
    val textColor = if (isBot) { Color.Black } else { Color.White }
    Column(modifier = Modifier
        .clip(
            RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isBot) {
                    0.dp
                } else {
                    20.dp
                },
                bottomEnd = if (isBot) {
                    20.dp
                } else {
                    0.dp
                }
            )
        )
        .background(backgroundColor)
        .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
        Text(text, color = textColor)
    }
}

