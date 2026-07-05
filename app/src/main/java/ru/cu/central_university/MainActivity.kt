package ru.cu.central_university

import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import ru.cu.central_university.cu.Screen
import ru.cu.central_university.ui.screen.MainScreen
import ru.cu.central_university.ui.theme.Central_UniversityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Central_UniversityTheme {
                MainScreen(
                    context = this,
                    onShowFileChooser = { callback, params ->
                        openFileChooser(callback, params)
                    }
                )
            }
        }
    }

    fun openFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: WebChromeClient.FileChooserParams
    ): Boolean {
        this.filePathCallback?.onReceiveValue(null)
        this.filePathCallback = filePathCallback

        return try {
            val intent = fileChooserParams.createIntent()
            fileChooserLauncher.launch(intent)
            true
        } catch (e: Exception) {
            this.filePathCallback = null
            false
        }
    }

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    private val fileChooserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val callback = filePathCallback
            filePathCallback = null

            if (callback == null) return@registerForActivityResult

            if (result.resultCode == RESULT_OK) {
                val data = result.data

                val results = WebChromeClient.FileChooserParams.parseResult(
                    result.resultCode,
                    data
                )

                callback.onReceiveValue(results)
            } else {
                callback.onReceiveValue(null)
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
