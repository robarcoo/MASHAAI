package com.example.mashaai.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.ChatInfo
import com.example.domain.models.Message
import com.example.mashaai.R


@Composable
fun ChatListScreen(innerPadding: PaddingValues) {
    val testData = listOf(ChatInfo(
        name = "Маша",
        messageList = List(10) { Message("Test Message", false) }),
        ChatInfo(
            name = "Маша",
            messageList = List(10) { Message("Test Message Test Message Test Message Test Message", false) }))
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(testData.size) { index ->
            val chat = testData[index]
            ChatItem(
                name = chat.name,
                lastMessage = chat.messageList[chat.messageList.size - 1].message,
                unreadMessagesCount = chat.messageList.count { !it.isRead })
        }
    }
}

@Composable
fun ChatItem(image: Int = R.drawable.ic_default_avatar, name : String, lastMessage : String, unreadMessagesCount : Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.padding(16.dp).weight(1f, fill = false),
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
        if (unreadMessagesCount > 0) {
            Text(unreadMessagesCount.toString(),
                modifier = Modifier.padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Red,
                            radius = this.size.maxDimension
                        )
                    },
                fontSize = 10.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChatListTopBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            ,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Чаты") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                    }
                },
                scrollBehavior = scrollBehavior

            )
        },
        bottomBar = {

        }

    ) { innerPadding ->
        ChatListScreen(innerPadding)
    }
}

