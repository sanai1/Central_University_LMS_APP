package ru.cu.central_university

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.cu.central_university.cu.Screen
import ru.cu.central_university.ui.component.WebViewScreen

@Composable
internal fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onShowFileChooser: (
        ValueCallback<Array<Uri>>,
        WebChromeClient.FileChooserParams
    ) -> Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TasksScreen.route,
        modifier = modifier
    ) {
        composable(Screen.CoursesScreen.route) {
            WebViewScreen(url = Screen.CoursesScreen.url, onShowFileChooser = onShowFileChooser)
        }
        composable(Screen.TasksScreen.route) {
            WebViewScreen(url = Screen.TasksScreen.url, onShowFileChooser = onShowFileChooser)
        }
        composable(Screen.GradeBookScreen.route) {
            WebViewScreen(url = Screen.GradeBookScreen.url, onShowFileChooser = onShowFileChooser)
        }
        composable(Screen.StatementScreen.route) {
            WebViewScreen(url = Screen.StatementScreen.url, onShowFileChooser = onShowFileChooser)
        }
        composable(Screen.HandBookScreen.route) {
            WebViewScreen(url = Screen.HandBookScreen.url, onShowFileChooser = onShowFileChooser)
        }
    }
}
