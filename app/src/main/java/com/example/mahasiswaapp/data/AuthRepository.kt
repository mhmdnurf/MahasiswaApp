package com.example.mahasiswaapp.data

import android.content.Context
import com.example.mahasiswaapp.model.AuthResponse
import com.example.mahasiswaapp.model.LoginRequest
import com.example.mahasiswaapp.model.RegisterRequest
import com.example.mahasiswaapp.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    context: Context,
    private val tokenProvider: AuthTokenProvider = AuthTokenProvider
) {
    private val apiService = NetworkModule.apiService
    private val authPreferences = AuthPreferences(context)

    val tokenFlow = authPreferences.tokenFlow

    suspend fun login(username: String, password: String): Result<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val body = response.body()?.data
                    if (body != null) {
                        persistToken(body.token)
                        Result.Success(body)
                    } else {
                        Result.Error(Exception("Response body kosong"))
                    }
                } else {
                    Result.Error(Exception(buildErrorMessage(response, "Gagal login")))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun register(
        username: String,
        password: String,
        email: String,
        nama: String
    ): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(RegisterRequest(username, password, email, nama))
            if (response.isSuccessful) {
                val body = response.body()?.data
                if (body != null) {
                    persistToken(body.token)
                    Result.Success(body)
                } else {
                    Result.Error(Exception("Response body kosong"))
                }
            } else {
                Result.Error(Exception(buildErrorMessage(response, "Registrasi gagal")))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun clearSession() {
        tokenProvider.setToken(null)
        authPreferences.clearToken()
    }

    suspend fun preloadToken() {
        tokenProvider.setToken(authPreferences.getToken())
    }

    private suspend fun persistToken(token: String) {
        tokenProvider.setToken(token)
        authPreferences.saveToken(token)
    }

    private fun buildErrorMessage(response: retrofit2.Response<*>, fallback: String): String {
        return parseErrorMessage(response) ?: "$fallback: ${response.message()}"
    }

    private fun parseErrorMessage(response: retrofit2.Response<*>): String? {
        val errorBody = response.errorBody() ?: return response.message().takeIf { it.isNotBlank() }
        return try {
            val json = NetworkModule.gson.fromJson(errorBody.charStream(), com.google.gson.JsonObject::class.java)
            json?.get("message")?.asString?.takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            response.message().takeIf { it.isNotBlank() }
        }
    }
}
