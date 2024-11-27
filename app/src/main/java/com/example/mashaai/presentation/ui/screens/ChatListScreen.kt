package com.example.mashaai.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import com.example.mashaai.R
import com.example.mashaai.viewmodels.ChatViewModel
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(innerPadding: PaddingValues, viewModel: ChatViewModel) {
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
                query = text, onQueryChange = { text = it },
                onSearch = { active = false }, active = active,
                onActiveChange = { active = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Поиск", fontSize = 14.sp) }) {

            }
        }
        items(chatList.value) { chat ->
            ChatItem(
                name = chat.name,
                lastMessage = chat.messageList[chat.messageList.size - 1].message
            )
        }
    }
}

@Composable
fun ChatItem(image: Int = R.drawable.ic_default_avatar, name : String, lastMessage : String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
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
            Column(modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(name, fontSize = 12.sp)
                Text(lastMessage, fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen() {
    val viewModel: ChatViewModel = getViewModel()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Чаты") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.createChat(
                        ChatInfo(
                            viewModel.chatListState.value.maxBy { it.id }.id + 1,
                            name = "Маша",
                            messageList = mutableListOf()
                        )
                    ) }) {
                        Icons.Filled.Add
                    }
                },
                scrollBehavior = scrollBehavior

            )
        },
        bottomBar = {
        }

    ) { innerPadding ->
        ChatList(innerPadding, viewModel)
    }
}

