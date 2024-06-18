package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartRateActivity : AppCompatActivity() {

    private lateinit var heartRateTextView: TextView
    private lateinit var lastHeartRateTextView: TextView
    private lateinit var lastCheckedTimeTextView: TextView
    private lateinit var handler: Handler
    private var currentIndex = 0
    private val heartRateData = listOf("086", "087", "088", "089", "090", "091") // Contoh data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.heart_rate)

        heartRateTextView = findViewById(R.id.heart_rate_text_view)
        lastHeartRateTextView = findViewById(R.id.last_heart_rate_text_view)
        lastCheckedTimeTextView = findViewById(R.id.last_checked_time_text_view)

        // Set up the back button
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            // Navigate back to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Get last check data
        getLastCheckData()

        // Start updating heart rate
        handler = Handler(Looper.getMainLooper())
        startHeartRateUpdate()
    }

    private fun startHeartRateUpdate() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentIndex < heartRateData.size) {
                    heartRateTextView.text = heartRateData[currentIndex]
                    currentIndex++
                    handler.postDelayed(this, 2000) // Update every 2 seconds
                }
            }
        }, 2000) // Initial delay
    }

    private fun getLastCheckData() {
        // Simulate API call
        CoroutineScope(Dispatchers.IO).launch {
            // Replace with actual API call
            val lastHeartRate = "091"
            val lastCheckedTime = "17:07:52"

            withContext(Dispatchers.Main) {
                lastHeartRateTextView.text = lastHeartRate
                lastCheckedTimeTextView.text = lastCheckedTime
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
