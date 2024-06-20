package com.test123.testmap

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationFamily(
    val username: String,
    val phoneNumber: String,
    val password: String
) : Parcelable
