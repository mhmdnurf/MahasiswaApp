package com.example.mahasiswaapp.data

import com.example.mahasiswaapp.model.Dosen
import com.example.mahasiswaapp.model.Mahasiswa
import com.example.mahasiswaapp.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository {
    private val apiService = NetworkModule.apiService

    fun getMahasiswa(): Flow<Result<List<Mahasiswa>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getMahasiswa()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    // Unwrap the ApiResponse to get the actual data
                    val data = apiResponse.data
                    if (data != null) {
                        emit(Result.Success(data))
                    } else {
                        emit(Result.Error(Exception(apiResponse.message ?: "Data mahasiswa kosong")))
                    }
                } ?: emit(Result.Error(Exception("Response body kosong")))
            } else {
                emit(Result.Error(Exception("Gagal mengambil data mahasiswa: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getDosen(): Flow<Result<List<Dosen>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDosen()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    // Unwrap the ApiResponse to get the actual data
                    val data = apiResponse.data
                    if (data != null) {
                        emit(Result.Success(data))
                    } else {
                        emit(Result.Error(Exception(apiResponse.message ?: "Data dosen kosong")))
                    }
                } ?: emit(Result.Error(Exception("Response body kosong")))
            } else {
                emit(Result.Error(Exception("Gagal mengambil data dosen: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
