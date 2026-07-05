package ru.cu.central_university

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.cu.central_university.MainActivity.Companion.screens
import ru.cu.central_university.cu.RouteScreen
import ru.cu.central_university.cu.Screen
import ru.cu.central_university.ui.component.WebViewScreen
import ru.cu.central_university.ui.screen.SettingsScreen

@Composable
internal fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onShowFileChooser: (
        ValueCallback<Array<Uri>>,
        WebChromeClient.FileChooserParams
    ) -> Boolean,
    onWebViewCreated: (WebView) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LMSScreen.subScreens.first().route,
        modifier = modifier
    ) {
        screens.forEach { screen ->
            when {
                screen.subScreens.isNotEmpty() -> {
                    screen.subScreens.forEach { subScreen ->
                        composable(subScreen.route) {
                            WebViewScreen(
                                url = subScreen.url
                                    ?: throw RuntimeException("Необходимо передавать URL в ${subScreen.route}"),
                                onShowFileChooser = onShowFileChooser,
                                onWebViewCreated = onWebViewCreated,
                            )
                        }
                    }
                }

                screen.url != null -> {
                    composable(screen.route) {
                        WebViewScreen(
                            url = screen.url,
                            onShowFileChooser = onShowFileChooser,
                            onWebViewCreated = onWebViewCreated,
                        )
                    }
                }

                else -> {
                    composable(screen.route) {
                        when (screen.route) {
                            RouteScreen.SETTINGS -> SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}

