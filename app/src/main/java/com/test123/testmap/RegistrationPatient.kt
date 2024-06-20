package com.test123.testmap

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationPatient(
    val username: String,
    val phoneNumber: String,
    val address: String,
    val dateOfBirth: String,
    val gender: String,
    val password: String

): Parcelable
