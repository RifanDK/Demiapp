package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddressActivity : AppCompatActivity() {

    private lateinit var homeAddressButton: LinearLayout
    private lateinit var destinationAddressButton: LinearLayout
    private lateinit var homeAddressTextView: TextView
    private lateinit var destinationAddressTextView: TextView

    private var homeAddress: String = ""
    private var destinationAddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_layout)

        homeAddressButton = findViewById(R.id.home_address_button)
        destinationAddressButton = findViewById(R.id.destination_address_button)
        homeAddressTextView = findViewById(R.id.home_address_text)
        destinationAddressTextView = findViewById(R.id.destination_address_text)

        homeAddressButton.setOnClickListener {
            val intent = Intent(this, MapAddressActivity::class.java)
            intent.putExtra("ADDRESS_TYPE", "HOME")
            startActivityForResult(intent, REQUEST_CODE_HOME_ADDRESS)
        }

        destinationAddressButton.setOnClickListener {
            val intent = Intent(this, MapAddressActivity::class.java)
            intent.putExtra("ADDRESS_TYPE", "DESTINATION")
            startActivityForResult(intent, REQUEST_CODE_DESTINATION_ADDRESS)
        }

        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Send data to backend and HistoryActivity
        val sendBackendButton: ImageButton = findViewById(R.id.send_backend_button)
        sendBackendButton.setOnClickListener {
            sendDataToBackend()
            sendDataToHistoryActivity()
        }
    }

    private fun sendDataToBackend() {
        //TODO
        // Kode untuk mengirim data ke backend


    }

    private fun sendDataToHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("HOME_ADDRESS", homeAddress)
        intent.putExtra("DESTINATION_ADDRESS", destinationAddress)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_HOME_ADDRESS -> {
                    homeAddress = data?.getStringExtra("ADDRESS") ?: ""
                    homeAddressTextView.text = homeAddress
                }
                REQUEST_CODE_DESTINATION_ADDRESS -> {
                    destinationAddress = data?.getStringExtra("ADDRESS") ?: ""
                    destinationAddressTextView.text = destinationAddress
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_HOME_ADDRESS = 1
        private const val REQUEST_CODE_DESTINATION_ADDRESS = 2
    }
}
