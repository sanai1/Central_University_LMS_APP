package ru.cu.central_university.ui.screen

import android.content.Context
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.cu.central_university.AppNavHost
import ru.cu.central_university.MainActivity.Companion.screens

@Composable
internal fun MainScreen(
    context: Context,
    onShowFileChooser: (
        ValueCallback<Array<Uri>>,
        WebChromeClient.FileChooserParams
    ) -> Boolean,
    onWebViewCreated: (WebView) -> Unit,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val lmsSubScreens = screens.firstOrNull { it.subScreens.isNotEmpty() }?.subScreens.orEmpty()
    val isLmsRoute = lmsSubScreens.any { it.route == currentRoute }

    var isLmsMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Column {
                if (isLmsMenuExpanded && lmsSubScreens.isNotEmpty()) {
                    NavigationBar {
                        lmsSubScreens.forEach { subScreen ->
                            NavigationBarItem(
                                selected = currentRoute == subScreen.route,
                                onClick = {
                                    isLmsMenuExpanded = false
                                    navController.navigateSingleTop(subScreen.route)
                                },
                                icon = {
                                    Icon(
                                        imageVector = subScreen.icon,
                                        contentDescription = context.getString(subScreen.title),
                                    )
                                },
                                label = {
                                    Text(text = context.getString(subScreen.title))
                                }
                            )
                        }
                    }
                }

                NavigationBar {
                    screens.forEach { screen ->
                        val isLms = screen.subScreens.isNotEmpty()
                        NavigationBarItem(
                            selected = if (isLms) isLmsMenuExpanded || isLmsRoute else currentRoute == screen.route,
                            onClick = {
                                if (isLms) {
                                    isLmsMenuExpanded = !isLmsMenuExpanded
                                } else {
                                    isLmsMenuExpanded = false
                                    navController.navigateSingleTop(screen.route)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = context.getString(screen.title),
                                )
                            },
                            label = {
                                Text(text = context.getString(screen.title))
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            onShowFileChooser = onShowFileChooser,
            onWebViewCreated = onWebViewCreated,
        )
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
