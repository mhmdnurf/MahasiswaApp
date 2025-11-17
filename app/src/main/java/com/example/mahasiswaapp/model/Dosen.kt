package com.example.mahasiswaapp.model

import com.google.gson.annotations.SerializedName

data class Dosen(
    @SerializedName("nama")
    val nama: String? = null,

    @SerializedName("nidn")
    val nidn: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("nomor_hp")
    val nomorHp: String? = null
)
