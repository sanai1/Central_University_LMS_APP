package ru.cu.central_university.ui.component

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast

fun WebView.applyDarkMode(darkTheme: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        settings.isAlgorithmicDarkeningAllowed = darkTheme
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        @Suppress("DEPRECATION")
        settings.forceDark =
            if (darkTheme) {
                WebSettings.FORCE_DARK_ON
            } else {
                WebSettings.FORCE_DARK_OFF
            }
    }

    setBackgroundColor(
        if (darkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    )
}

fun downloadFile(
    context: Context,
    url: String,
    userAgent: String?,
    contentDisposition: String?,
    mimeType: String?
) {
    try {
        val fileName = URLUtil.guessFileName(
            url,
            contentDisposition,
            mimeType
        )

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle(fileName)
            setDescription("Скачивание файла")
            setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )

            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )

            addRequestHeader("User-Agent", userAgent ?: "")

            CookieManager.getInstance()
                .getCookie(url)
                ?.let { cookies ->
                    addRequestHeader("Cookie", cookies)
                }

            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(
            context,
            "Файл скачивается: $fileName",
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        Log.e("WEBVIEW_DOWNLOAD", "Download error", e)
        Toast.makeText(
            context,
            "Не удалось скачать файл",
            Toast.LENGTH_SHORT
        ).show()
    }
}
