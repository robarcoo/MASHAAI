package com.example.mashaai.presentation.ui.screens


import android.util.Log
import androidx.activity.ComponentActivity
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import com.example.mashaai.R
import com.example.mashaai.presentation.navigation.LocalNavController
import com.example.mashaai.presentation.navigation.Route
import com.example.mashaai.presentation.ui.theme.Blue
import com.example.mashaai.presentation.ui.theme.Gray
import com.example.mashaai.presentation.ui.theme.LightGray
import com.example.mashaai.viewmodels.ChatViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(innerPadding: PaddingValues, viewModel: ChatViewModel) {
    val navController = LocalNavController.current!!
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val chatList = viewModel.chatListState.collectAsState()
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        item {
            SearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                windowInsets = WindowInsets(top = 0.dp),
                shape = RoundedCornerShape(24.dp),
                query = text, onQueryChange = { text = it },
                onSearch = { active = false }, active = active,
                onActiveChange = { active = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null,
                    tint = White) },
                placeholder = { Text("Поиск", fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.lack_regular)),
                    color = White) },
                enabled = false,
                colors = SearchBarDefaults.colors(
                    containerColor = Blue,
                    dividerColor = Color.Transparent
                )) {

            }
        }
        items(chatList.value) { chat ->
            ChatItem(
                name = chat.name,
                image = chat.image ?: R.drawable.ic_default_avatar,
                lastMessage = chat.messageList[chat.messageList.size - 1].message
            ) {
                navController.navigate(Route.ChatRoomScreen.withArgs(chat.id.toString()))
            }
        }
    }
}

@Composable
fun ChatItem(image: Int, name : String, lastMessage : String, onClick : () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier
            .padding(16.dp)
            .weight(1f, fill = false),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(painter = painterResource(id = image),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .size(40.dp),
                contentDescription = "Chat Avatar")
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(name.uppercase(), fontSize = 12.sp,
                    color = Blue,
                    fontFamily = FontFamily(Font(R.font.polonium_bold)),
                    lineHeight = 16.sp
                )
                Text(lastMessage, fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontFamily = FontFamily(Font(R.font.lack_regular)),
                    color = Gray,
                    lineHeight = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen() {
    val viewModel: ChatViewModel = koinViewModel<ChatViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val chatList = viewModel.chatListState.collectAsState()
    val chatId = if (chatList.value.isNotEmpty()) {
        chatList.value.maxBy { it.id }.id + 1
    } else {
        0
    }
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ЧАТЫ",
                    color = Blue,
                    fontFamily = FontFamily(Font(R.font.polonium_bold)),
                    fontSize = 12.sp) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.createChat(
                        ChatInfo(
                            chatId,
                            name = "Маша",
                            image = R.drawable.masha_color,
                            messageList = mutableListOf(
                                Message(id = 0,
                                    chatId = chatId,
                                    message = "Привет, что ты хочешь у меня спросить?\nЧасто задаваемые вопросы:\nКак получить общежитие?\nГде находятся здания университета?"
                                    )
                            )
                        )
                    ) }
                        ) {
                        Icon(Icons.Filled.Add, contentDescription = "Добавить чат",
                            tint = Blue)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },

    ) { innerPadding ->
        ChatList(innerPadding, viewModel)
    }
}

