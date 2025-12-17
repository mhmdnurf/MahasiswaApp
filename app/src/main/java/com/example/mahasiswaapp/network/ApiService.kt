package com.example.mahasiswaapp.network

import com.example.mahasiswaapp.model.ApiResponse
import com.example.mahasiswaapp.model.AuthResponse
import com.example.mahasiswaapp.model.CreateDosenRequest
import com.example.mahasiswaapp.model.CreateMahasiswaRequest
import com.example.mahasiswaapp.model.Dosen
import com.example.mahasiswaapp.model.LoginRequest
import com.example.mahasiswaapp.model.Mahasiswa
import com.example.mahasiswaapp.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>

    @GET("mahasiswa")
    suspend fun getMahasiswa(): Response<ApiResponse<List<Mahasiswa>>>

    @GET("mahasiswa/{nim}")
    suspend fun getMahasiswaByNim(
        @Path("nim") nim: String
    ): Response<ApiResponse<Mahasiswa>>

    @POST("mahasiswa")
    suspend fun createMahasiswa(
        @Body request: CreateMahasiswaRequest
    ): Response<ApiResponse<Mahasiswa>>

    @PUT("mahasiswa/{nim}")
    suspend fun updateMahasiswa(
        @Path("nim") nim: String,
        @Body request: CreateMahasiswaRequest
    ): Response<ApiResponse<Mahasiswa>>

    @DELETE("mahasiswa/{nim}")
    suspend fun deleteMahasiswa(
        @Path("nim") nim: String
    ): Response<ApiResponse<Unit>>

    @GET("dosen")
    suspend fun getDosen(): Response<ApiResponse<List<Dosen>>>

    @GET("dosen/{nidn}")
    suspend fun getDosenByNidn(
        @Path("nidn") nidn: String
    ): Response<ApiResponse<Dosen>>

    @POST("dosen")
    suspend fun createDosen(
        @Body request: CreateDosenRequest
    ): Response<ApiResponse<Dosen>>

    @PUT("dosen/{nidn}")
    suspend fun updateDosen(
        @Path("nidn") nidn: String,
        @Body request: CreateDosenRequest
    ): Response<ApiResponse<Dosen>>

    @DELETE("dosen/{nidn}")
    suspend fun deleteDosen(
        @Path("nidn") nidn: String
    ): Response<ApiResponse<Unit>>
}
