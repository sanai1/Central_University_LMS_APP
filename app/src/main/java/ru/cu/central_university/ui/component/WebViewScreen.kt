package ru.cu.central_university.ui.component

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebViewScreen(
    url: String,
    modifier: Modifier = Modifier,
    onShowFileChooser: (
        ValueCallback<Array<Uri>>,
        WebChromeClient.FileChooserParams
    ) -> Boolean
) {
    val darkTheme = isSystemInDarkTheme()
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // включение Java Script на сайте
                settings.javaScriptEnabled = true
                settings.apply {
                    // важно для SPA-сайтов, личных кабинетов, сайтов на React/Vue/Angular
                    domStorageEnabled = true
                    // автозагрузка изображений
                    loadsImagesAutomatically = true
                    blockNetworkImage = false
                    // адоптация под размер экрана
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    // масштабирование жестами
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                    // для хранения данных
                    databaseEnabled = true
                    // открытие окон через JS
                    javaScriptCanOpenWindowsAutomatically = true
                    // убрать полосы прокрутки
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onShowFileChooser(
                        webView: WebView?,
                        filePathCallback: ValueCallback<Array<Uri>>?,
                        fileChooserParams: FileChooserParams?
                    ): Boolean {
                        if (filePathCallback == null || fileChooserParams == null) {
                            return false
                        }
                        return onShowFileChooser(filePathCallback, fileChooserParams)
                    }
                }
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.applyDarkMode(darkTheme)
        }
    )
}

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
