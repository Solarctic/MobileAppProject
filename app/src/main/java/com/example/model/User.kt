package com.example.model

data class User(
    val id: String? = null,  // Nullable, server assigns this
    val username: String,
    val password: String
)

