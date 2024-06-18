package com.test123.testmap

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.test123.testmap.R
import java.util.*

class PatientRegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var dateOfBirthTextview: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var passwordEditText: EditText
    private lateinit var retypePasswordEditText: EditText
    private lateinit var registerButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var datePickerButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_form_patient)

        // Initialize views
        usernameEditText = findViewById(R.id.username_edit_text)
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text)
        addressEditText = findViewById(R.id.address_edit_text)
        dateOfBirthTextview = findViewById(R.id.et_date_of_birth)
        genderRadioGroup = findViewById(R.id.gender_radio_group)
        maleRadioButton = findViewById(R.id.rb_male)
        femaleRadioButton = findViewById(R.id.rb_female)
        passwordEditText = findViewById(R.id.password_edit_text)
        retypePasswordEditText = findViewById(R.id.retype_password_edit_text)
        registerButton = findViewById(R.id.register_button)
        backButton = findViewById(R.id.kembali_button)
        datePickerButton = findViewById(R.id.btn_date_picker)

        // Set click listeners
        registerButton.setOnClickListener {
            registerUser()
        }

        backButton.setOnClickListener {
            // Navigate back to the profile switch activity
            val intent = Intent(this, ProfileSwitchActivity::class.java)
            startActivity(intent)
        }

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        dateOfBirthTextview.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateOfBirthTextview.setText(formattedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun registerUser() {
        val username = usernameEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()
        val dateOfBirth = dateOfBirthTextview.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val retypePassword = retypePasswordEditText.text.toString().trim()

        if (username.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty() ||
            password.isEmpty() || retypePassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != retypePassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.rb_male -> "Laki Laki"
            R.id.rb_female -> "Perempuan"
            else -> ""
        }

        if (gender.isEmpty()) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
            return
        }

        val registrationData = RegistrationPatient(
            username, phoneNumber, address, dateOfBirth, gender, password
        )

        // TODO: Send this data to the backend API

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

        // Navigate back to the profile switch activity
        val intent = Intent(this, ProfileSwitchActivity::class.java)
        startActivity(intent)
    }
}
