package com.example.mahasiswaapp.data

import com.example.mahasiswaapp.model.CreateDosenRequest
import com.example.mahasiswaapp.model.CreateMahasiswaRequest
import com.example.mahasiswaapp.model.Dosen
import com.example.mahasiswaapp.model.Mahasiswa
import com.example.mahasiswaapp.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

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
    }.flowOn(Dispatchers.IO)

    fun getMahasiswaDetail(nim: String): Flow<Result<Mahasiswa>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getMahasiswaByNim(nim)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val data = apiResponse.data
                    if (data != null) {
                        emit(Result.Success(data))
                    } else {
                        emit(Result.Error(Exception(apiResponse.message ?: "Data mahasiswa tidak ditemukan")))
                    }
                } ?: emit(Result.Error(Exception("Response body kosong")))
            } else {
                emit(Result.Error(Exception("Gagal mengambil detail mahasiswa: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun createMahasiswa(request: CreateMahasiswaRequest): Result<Mahasiswa> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.createMahasiswa(request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        Result.Success(data)
                    } ?: Result.Error(Exception("Response body kosong"))
                } else {
                    Result.Error(Exception("Gagal menambahkan mahasiswa: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun updateMahasiswa(nim: String, request: CreateMahasiswaRequest): Result<Mahasiswa> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateMahasiswa(nim, request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        Result.Success(data)
                    } ?: Result.Error(Exception("Response body kosong"))
                } else {
                    Result.Error(Exception("Gagal memperbarui mahasiswa: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun deleteMahasiswa(nim: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteMahasiswa(nim)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Gagal menghapus mahasiswa: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    fun getDosen(): Flow<Result<List<Dosen>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDosen()
            if (response.isSuccessful) {
                response.body()?.data?.let { data ->
                    emit(Result.Success(data))
                } ?: emit(Result.Error(Exception("Response body kosong")))
            } else {
                emit(Result.Error(Exception("Gagal mengambil data dosen: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    fun getDosenDetail(nidn: String): Flow<Result<Dosen>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDosenByNidn(nidn)
            if (response.isSuccessful) {
                response.body()?.data?.let { data ->
                    emit(Result.Success(data))
                } ?: emit(Result.Error(Exception("Response body kosong")))
            } else {
                emit(Result.Error(Exception("Gagal mengambil data dosen: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun createDosen(request: CreateDosenRequest): Result<Dosen> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.createDosen(request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        Result.Success(data)
                    } ?: Result.Error(Exception("Response body kosong"))
                } else {
                    Result.Error(Exception("Gagal menambahkan dosen: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun updateDosen(nidn: String, request: CreateDosenRequest): Result<Dosen> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateDosen(nidn, request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        Result.Success(data)
                    } ?: Result.Error(Exception("Response body kosong"))
                } else {
                    Result.Error(Exception("Gagal memperbarui dosen: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun deleteDosen(nidn: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteDosen(nidn)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Gagal menghapus dosen: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
