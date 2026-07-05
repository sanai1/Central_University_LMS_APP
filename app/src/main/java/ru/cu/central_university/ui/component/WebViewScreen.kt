package ru.cu.central_university.ui.component

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Message
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
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
                    setSupportMultipleWindows(true)
                    javaScriptCanOpenWindowsAutomatically = true
                    // убрать полосы прокрутки
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView?,
                        isDialog: Boolean,
                        isUserGesture: Boolean,
                        resultMsg: Message?
                    ): Boolean {
                        val context = view?.context ?: return false
                        val newWebView = WebView(context)

                        newWebView.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val newUrl = request?.url?.toString() ?: return false
                                downloadFile(
                                    context = context,
                                    url = newUrl,
                                    userAgent = this@apply.settings.userAgentString,
                                    contentDisposition = null,
                                    mimeType = null
                                )
                                return true
                            }
                        }
                        val transport = resultMsg?.obj as? WebView.WebViewTransport
                        transport?.webView = newWebView
                        resultMsg?.sendToTarget()
                        return true
                    }

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
                setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
                    downloadFile(
                        context = context,
                        url = url,
                        userAgent = userAgent,
                        contentDisposition = contentDisposition,
                        mimeType = mimeType
                    )
                }
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.applyDarkMode(darkTheme)
        }
    )
}
