package com.example.mahasiswaapp.data

object AuthTokenProvider {
    @Volatile
    private var token: String? = null

    fun setToken(value: String?) {
        token = value
    }

    fun getToken(): String? = token
}
