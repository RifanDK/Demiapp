package com.test123.testmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class PatientItem(val name: String, val phone: String)

class PatientAdapter(private val patientList: List<PatientItem>) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient_list, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patientItem = patientList[position]
        holder.patientNameTextView.text = patientItem.name
        holder.patientPhoneTextView.text = patientItem.phone
    }

    override fun getItemCount() = patientList.size

    class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientNameTextView: TextView = itemView.findViewById(R.id.patient_name)
        val patientPhoneTextView: TextView = itemView.findViewById(R.id.patient_phone)
    }
}
