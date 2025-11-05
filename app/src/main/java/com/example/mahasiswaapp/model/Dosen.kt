package com.example.mahasiswaapp.model

import com.google.gson.annotations.SerializedName

data class Dosen(
    @SerializedName("nama")
    val nama: String? = null,
    
    @SerializedName("nidn")
    val nidn: String? = null,
    
    @SerializedName("prodi")
    val prodi: String? = null,
    
    @SerializedName("bidangKeahlian", alternate = ["bidang_keahlian"])
    val bidangKeahlian: String? = null
)
