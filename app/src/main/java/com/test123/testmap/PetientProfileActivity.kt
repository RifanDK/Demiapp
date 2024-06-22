package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PatientProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var usernameText: TextView
    private lateinit var phoneNumberText: TextView
    private lateinit var addressText: TextView
    private lateinit var familyText: TextView
    private lateinit var familyPhoneNumberText: TextView
    private lateinit var loginAsText: TextView
    private lateinit var logoutButton: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_patient_layout)

        // Initialize views
        profilePicture = findViewById(R.id.profile_picture)
        usernameText = findViewById(R.id.username_text)
        phoneNumberText = findViewById(R.id.phone_number_text)
        addressText = findViewById(R.id.address_text)
        familyText = findViewById(R.id.family_text)
        familyPhoneNumberText = findViewById(R.id.family_phone_number_text)
        loginAsText = findViewById(R.id.login_as_text)
        logoutButton = findViewById(R.id.logout_button)
        backButton = findViewById(R.id.back_button)

        // Get data from intent
        val registrationPatient = intent.getParcelableExtra<RegistrationPatient>("RegistrationPatient")
        val loginAs = intent.getStringExtra("LoginAs")

        // Set data to views
        if (registrationPatient != null) {
            usernameText.text = registrationPatient.username
            phoneNumberText.text = registrationPatient.phoneNumber
            addressText.text = registrationPatient.address

            // Assuming you have a method to get family data from backend
            getFamilyData()
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
            // Navigate back to MainActivityPatient
            val intent = Intent(this@PatientProfileActivity, MainActivityPatient::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getFamilyData() {
        // TODO: Implement API call to get family data
        // For now, setting some dummy data
        familyText.text = "Keluarga Contoh"
        familyPhoneNumberText.text = "08123456789"
    }
}
