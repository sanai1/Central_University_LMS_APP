package ru.cu.central_university

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.cu.central_university.cu.Screen
import ru.cu.central_university.ui.screen.MainScreen
import ru.cu.central_university.ui.theme.Central_UniversityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Central_UniversityTheme {
                MainScreen(this)
            }
        }
    }

    companion object {
        internal val screens = listOf(
            Screen.CoursesScreen,
            Screen.TasksScreen,
            Screen.GradeBookScreen,
            Screen.StatementScreen,
            Screen.HandBookScreen,
        )
    }
}
