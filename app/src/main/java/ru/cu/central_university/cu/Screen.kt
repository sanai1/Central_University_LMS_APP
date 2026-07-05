package ru.cu.central_university.cu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Message
import androidx.compose.ui.graphics.vector.ImageVector
import ru.cu.central_university.R

internal sealed class Screen(
    val route: String,
    val title: Int,
    val url: String,
    val icon: ImageVector,
) {
    object CoursesScreen : Screen(
        route = "courses",
        title = R.string.menu_title_courses,
        url = CentralUniversityURL.COURSES,
        icon = Icons.Default.MenuBook,
    )
    object TasksScreen : Screen(
        route = "tasks",
        title = R.string.menu_title_tasks,
        url = CentralUniversityURL.TASKS,
        icon = Icons.Default.Folder,
    )
    object TimeSrceen : Screen(
        route = "time",
        title = R.string.menu_title_time,
        url = CentralUniversityURL.TIME,
        icon = Icons.Default.Message,
    )
    object GradeBookScreen : Screen(
        route = "grade_book",
        title = R.string.menu_title_grade_book,
        url = CentralUniversityURL.GRADE_BOOK,
        icon = Icons.Default.Grade,
    )
    object StatementScreen : Screen(
        route = "statement",
        title = R.string.menu_title_statement,
        url = CentralUniversityURL.STATEMENT,
        icon = Icons.Default.Book,
    )
    object HandBookScreen : Screen(
        route = "hand_book",
        title = R.string.menu_title_handbook,
        url = CentralUniversityURL.HAND_BOOK,
        icon = Icons.Default.LocalLibrary,
    )
}
