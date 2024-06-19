package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_form)

        // Initialize views
        usernameEditText = findViewById(R.id.username_edittext)
        passwordEditText = findViewById(R.id.password_edittext)
        loginButton = findViewById(R.id.login_button)
        registerTextView = findViewById(R.id.goes_profileswitch)

        // Set click listeners
        loginButton.setOnClickListener {
            loginUser()
        }

        registerTextView.setOnClickListener {
            // Navigate to ProfileSwitchActivity
            val intent = Intent(this@LoginActivity, ProfileSwitchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val loginData = LoginData(username, password)

        // TODO: Send this data to the backend API for verification

        // Simulating a successful login for demonstration purposes
        if (authenticateUser(loginData)) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

            // Navigate to the main activity or home screen upon successful login
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Login failed. Please check your credentials", Toast.LENGTH_SHORT).show()
        }
    }

    // Dummy function to simulate user authentication
    private fun authenticateUser(loginData: LoginData): Boolean {
        // Replace this with actual backend authentication
        return loginData.loginusername == "testuser" && loginData.loginpassword == "testpassword"
    }
}
