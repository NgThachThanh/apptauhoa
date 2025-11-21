package com.example.apptauhoa.data.model

data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val role: String, // "admin" or "user"
    val phone: String = "", // Mới
    val dob: String = ""    // Mới
)