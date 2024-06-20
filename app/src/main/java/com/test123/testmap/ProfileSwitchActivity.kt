package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfileSwitchActivity : AppCompatActivity() {

    private lateinit var asPatientButton: ImageButton
    private lateinit var asFamilyButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_switch)

        asPatientButton = findViewById(R.id.as_patient_button)
        asFamilyButton = findViewById(R.id.as_family_button)

        asPatientButton.setOnClickListener {
            val intent = Intent(this@ProfileSwitchActivity, PatientRegisterActivity::class.java)
            intent.putExtra("role", "Pasien")
            startActivity(intent)
        }

        asFamilyButton.setOnClickListener {
            val intent = Intent(this@ProfileSwitchActivity, FamilyRegisterActivity::class.java)
            intent.putExtra("role", "Keluarga")
            startActivity(intent)
        }
    }
}
