package com.example.mashaai.presentation.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mashaai.R
import com.example.mashaai.presentation.ui.theme.Blue
import com.example.mashaai.presentation.ui.theme.LightGray
import com.example.mashaai.presentation.ui.theme.White
import com.example.mashaai.viewmodels.ChatViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(id : Int) {
    val viewModel : ChatViewModel = koinViewModel<ChatViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val chatList = viewModel.chatListState.collectAsState()
    val chatInfo = chatList.value.find { it.id == id } ?: throw IllegalStateException()
    val text = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(chatInfo.name.uppercase(),
                fontFamily = FontFamily(Font(R.font.polonium_bold)),
                color = Blue
            ) },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                navigationIcon = { Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Вернуться к списку диалогов") },
                actions = { Image(
                    painter = painterResource(id = chatInfo.image ?: R.drawable.ic_default_avatar),
                    contentDescription = "Аватар чат-бота",
                    modifier = Modifier.size(40.dp))},
            )
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
                        trailingIcon = { IconButton(onClick = {
                            viewModel.sendMessage(chatInfo, text.value)
                            text.value = ""
                        }) {
                            Image(
                                painterResource(R.drawable.ic_send),
                                contentDescription = "Отправить сообщение"
                            )
                        }
                        },
                        placeholder = { Text("Напиши, что хочешь",
                            fontFamily = FontFamily(Font(R.font.lack_regular)),
                            color = Black,
                            fontSize = 14.sp
                        ) },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = LightGray,
                            unfocusedContainerColor = LightGray
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
                    )
                }

            }
            
        }

    ) { innerPadding ->
        ChatRoomMessageList(innerPadding, viewModel, id)
    }
}



@Composable
fun ChatRoomMessageList(innerPadding: PaddingValues, viewModel: ChatViewModel, id: Int) {
    val chatList = viewModel.chatListState.collectAsState()
    val chatInfo = chatList.value.find { it.id == id } ?: throw IllegalStateException()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(items = chatInfo.messageList,
                key = { it.id }) { message ->
                Row(
                    modifier = Modifier
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
                        isBot = message.isBotAnswer
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBox(text: String, isBot: Boolean) {
    val backgroundColor = if (isBot) { LightGray } else { Blue }
    val textColor = if (isBot) { Black } else { White }
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
        Text(text, color = textColor,
            fontFamily = FontFamily(Font(R.font.lack_regular)),
            fontSize = 14.sp)
    }
}

