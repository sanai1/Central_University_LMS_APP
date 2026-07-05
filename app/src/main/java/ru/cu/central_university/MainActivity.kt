package ru.cu.central_university

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import ru.cu.central_university.cu.Screen
import ru.cu.central_university.ui.screen.MainScreen
import ru.cu.central_university.ui.theme.Central_UniversityTheme

class MainActivity : ComponentActivity() {
    private var currentWebView: WebView? = null
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private val smsConsentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                val code = extractCodeFromSms(message)
                if (code != null) {
                    insertSmsCodeIntoWebView(code)
                }
            }
        }
    private val smsConsentReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                val extras = intent.extras
                val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status

                when (status?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        if (consentIntent != null) {
                            smsConsentLauncher.launch(consentIntent)
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // SMS не пришла за отведённое время
                    }
                }
            }
        }
    }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSmsUserConsent()
        enableEdgeToEdge()
        setContent {
            Central_UniversityTheme {
                MainScreen(
                    context = this,
                    onShowFileChooser = { callback, params ->
                        openFileChooser(callback, params)
                    },
                    onWebViewCreated = { webView ->
                        currentWebView = webView
                    },
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()

        ContextCompat.registerReceiver(
            this,
            smsConsentReceiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
            SmsRetriever.SEND_PERMISSION,
            null,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(smsConsentReceiver)
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

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(this)
            .startSmsUserConsent(null)
    }

    private fun extractCodeFromSms(message: String?): String? {
        if (message.isNullOrBlank()) return null

        return Regex("""\b\d{4,8}\b""")
            .find(message)
            ?.value
    }

    private fun insertSmsCodeIntoWebView(code: String) {
        currentWebView?.evaluateJavascript(
            """
        (function() {
            var input = document.querySelector(
                'input[autocomplete="one-time-code"], input[type="tel"], input[type="number"], input[type="text"]'
            );

            if (input) {
                input.focus();
                input.value = '$code';
                input.dispatchEvent(new Event('input', { bubbles: true }));
                input.dispatchEvent(new Event('change', { bubbles: true }));
            }
        })();
        """.trimIndent(),
            null
        )
    }


    companion object {
        internal val screens = listOf(
            Screen.LMSScreen,
            Screen.CalendarScreen,
            Screen.TimeSrceen,
            Screen.HandBookScreen,
            Screen.SettingsScreen,
        )
    }
}
