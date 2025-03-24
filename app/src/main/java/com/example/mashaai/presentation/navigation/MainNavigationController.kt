package com.example.mashaai.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mashaai.presentation.ui.screens.ChatListScreen
import com.example.mashaai.presentation.ui.screens.ChatRoomScreen

@Composable
fun MainNavigationController(
    navController: NavHostController,
) {
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.ChatListScreen.path,
            enterTransition = { fadeIn(animationSpec = tween(350)) },
            exitTransition = { fadeOut(animationSpec = tween(350)) },
        ) {

            composable(route = Route.ChatListScreen.path) {
                ChatListScreen()
            }
            composable(route = Route.ChatRoomScreen.path + "/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                })) { entry ->
                entry.arguments?.getInt("id").let { id ->
                    if (id != null) {
                        ChatRoomScreen()
                    }
                }
            }
        }

        }
    }