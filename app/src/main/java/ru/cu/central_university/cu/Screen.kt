package ru.cu.central_university.cu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import ru.cu.central_university.R

internal sealed class Screen(
    val route: String,
    val title: Int,
    val url: String?,
    val icon: ImageVector,
    val subScreens: List<Screen> = emptyList(),
) {
    object LMSScreen : Screen(
        route = RouteScreen.LMS,
        title = R.string.menu_title_lms,
        url = null,
        icon = Icons.Default.AcUnit,
        subScreens = listOf(
            CoursesScreen,
            TasksScreen,
            GradeBookScreen,
            StatementScreen,
            LessonsScreen,
            HelpScreen,
        )
    )

    object CalendarScreen : Screen(
        route = RouteScreen.CALENDAR,
        title = R.string.menu_title_calendar,
        url = CentralUniversityURL.CALENDAR,
        icon = Icons.Default.CalendarMonth,
    )

    object TimeSrceen : Screen(
        route = RouteScreen.TIME,
        title = R.string.menu_title_time,
        url = CentralUniversityURL.TIME,
        icon = Icons.Default.Message,
    )

    object HandBookScreen : Screen(
        route = RouteScreen.HAND_BOOK,
        title = R.string.menu_title_handbook,
        url = CentralUniversityURL.HAND_BOOK,
        icon = Icons.Default.LocalLibrary,
    )

    object SettingsScreen : Screen(
        route = RouteScreen.SETTINGS,
        title = R.string.menu_title_settings,
        url = null,
        icon = Icons.Default.Settings,
    )

    companion object {
        object CoursesScreen : Screen(
            route = RouteScreen.LMS_COURSES,
            title = R.string.menu_title_courses,
            url = CentralUniversityURL.LMS_COURSES,
            icon = Icons.Default.MenuBook,
        )

        object TasksScreen : Screen(
            route = RouteScreen.LMS_TASKS,
            title = R.string.menu_title_tasks,
            url = CentralUniversityURL.LMS_TASKS,
            icon = Icons.Default.Folder,
        )

        object GradeBookScreen : Screen(
            route = RouteScreen.LMS_GRADE_BOOK,
            title = R.string.menu_title_grade_book,
            url = CentralUniversityURL.LMS_GRADE_BOOK,
            icon = Icons.Default.Grade,
        )

        object StatementScreen : Screen(
            route = RouteScreen.LMS_STATEMEN,
            title = R.string.menu_title_statement,
            url = CentralUniversityURL.LMS_STATEMENT,
            icon = Icons.Default.Book,
        )

        object LessonsScreen : Screen(
            route = RouteScreen.LMS_LESSONS,
            title = R.string.menu_title_lessons,
            url = CentralUniversityURL.LMS_LESSONS,
            icon = Icons.Default.Book,
        )

        object HelpScreen : Screen(
            route = RouteScreen.LMS_HELP,
            title = R.string.menu_title_help,
            url = CentralUniversityURL.LMS_HELP,
            icon = Icons.Default.ElectricBolt,
        )
    }
}
