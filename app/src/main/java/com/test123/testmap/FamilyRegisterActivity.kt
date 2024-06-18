package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FamilyRegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var retypePasswordEditText: EditText
    private lateinit var registerButton: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_form_keluarga)

        // Initialize views
        usernameEditText = findViewById(R.id.username_edit_text)
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        retypePasswordEditText = findViewById(R.id.retype_password_edit_text)
        registerButton = findViewById(R.id.register_button)
        backButton = findViewById(R.id.kembali_button)

        // Set click listeners
        registerButton.setOnClickListener {
            registerUser()
        }

        backButton.setOnClickListener {
            // Navigate back to ProfileSwitchActivity
            val intent = Intent(this@FamilyRegisterActivity, ProfileSwitchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val username = usernameEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val retypePassword = retypePasswordEditText.text.toString().trim()

        if (username.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != retypePassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val registrationData = RegistrationFamily(username, phoneNumber, password)

        // TODO: Send this data to the backend API

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
    }
}
