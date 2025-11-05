package com.example.mahasiswaapp.network

import com.example.mahasiswaapp.model.ApiResponse
import com.example.mahasiswaapp.model.Dosen
import com.example.mahasiswaapp.model.Mahasiswa
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("mahasiswa")
    suspend fun getMahasiswa(): Response<ApiResponse<List<Mahasiswa>>>

    @GET("dosen")
    suspend fun getDosen(): Response<ApiResponse<List<Dosen>>>
}
