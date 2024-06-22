package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FamilyProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var usernameText: TextView
    private lateinit var phoneNumberText: TextView
    private lateinit var loginAsText: TextView
    private lateinit var logoutButton: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.family_profile_layout)

        // Initialize views
        profilePicture = findViewById(R.id.profile_picture)
        usernameText = findViewById(R.id.username_text)
        phoneNumberText = findViewById(R.id.phone_number_text)
        loginAsText = findViewById(R.id.login_as_text)
        logoutButton = findViewById(R.id.logout_button)
        backButton = findViewById(R.id.back_button)

        // Get data from intent
        val registrationFamily = intent.getParcelableExtra<RegistrationFamily>("RegistrationFamily")
        val loginAs = intent.getStringExtra("LoginAs")

        // Set data to views
        if (registrationFamily != null) {
            usernameText.text = registrationFamily.username
            phoneNumberText.text = registrationFamily.phoneNumber
        }

        // Set login as text
        if (!loginAs.isNullOrEmpty()) {
            loginAsText.text = loginAs
        } else {
            loginAsText.text = "Role not specified"
        }

        // Set click listeners
        logoutButton.setOnClickListener {
            // Handle logout logic here
            // For now, just finishing the activity
            finish()
        }

        backButton.setOnClickListener {
            // Navigate back to ProfileSwitchActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
