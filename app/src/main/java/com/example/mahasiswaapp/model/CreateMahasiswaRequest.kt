package com.example.mahasiswaapp.model

import com.google.gson.annotations.SerializedName

data class CreateMahasiswaRequest(
    @SerializedName("nama_lengkap")
    val namaLengkap: String,

    @SerializedName("nim")
    val nim: String,

    @SerializedName("jurusan")
    val jurusan: String? = null,

    @SerializedName("tahun_angkatan")
    val tahunAngkatan: Int? = null,

    @SerializedName("ipk")
    val ipk: Double? = null
)
