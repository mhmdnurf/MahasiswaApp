package com.example.mahasiswaapp.model

import com.google.gson.annotations.SerializedName

data class CreateDosenRequest(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("nidn")
    val nidn: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("prodi")
    val prodi: String,

    @SerializedName("konsentrasi")
    val konsentrasi: String
)
