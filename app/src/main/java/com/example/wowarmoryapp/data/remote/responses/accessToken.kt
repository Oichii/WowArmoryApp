package com.example.wowarmoryapp.data.remote.responses

data class accessToken(
    val access_token: String,
    val expires_in: Int,
    val scope: String,
    val token_type: String
)