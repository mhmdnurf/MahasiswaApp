package com.example.mahasiswaapp.model

import com.google.gson.annotations.SerializedName

data class Mahasiswa(
    @SerializedName("nama_lengkap")
    val nama_lengkap: String? = null,
    
    @SerializedName("nim")
    val nim: String? = null,
    
    @SerializedName("jurusan")
    val jurusan: String? = null,

    @SerializedName("tahun_angkatan")
    val tahun_angkatan: Int? = null,
    
    @SerializedName("ipk")
    val ipk: Double? = null
)
