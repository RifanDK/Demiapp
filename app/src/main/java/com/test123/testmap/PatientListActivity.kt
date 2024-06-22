package com.test123.testmap

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListPasienActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_list)

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Contoh data pasien
        val patientList = listOf(
            PatientItem("Pasien A", "081234567890"),
            PatientItem("Pasien B", "081234567891"),
            PatientItem("Pasien C", "081234567892")
        )

        // Set adapter
        recyclerView.adapter = PatientAdapter(patientList)

        // Set up back button
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
