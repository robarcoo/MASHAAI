package com.example.mashaai.presentation.navigation

sealed class Route(val path: String) {
    data object ChatListScreen : Route(path = "chat_list_screen")
    data object ChatRoomScreen : Route(path = "chat_room_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}