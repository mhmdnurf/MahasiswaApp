package com.example.mahasiswaapp.viewmodel

data class FieldErrors(
    val nama: String? = null,
    val nim: String? = null,
    val jurusan: String? = null,
    val tahunAngkatan: String? = null,
    val ipk: String? = null
) {
    val hasErrors: Boolean
        get() = listOf(nama, nim, jurusan, tahunAngkatan, ipk).any { it != null }
}

data class ValidationResult(
    val fieldErrors: FieldErrors = FieldErrors(),
    val generalError: String? = null
)

fun validateMahasiswaForm(state: MahasiswaFormUiState): ValidationResult? {
    val errors = FieldErrors(
        nama = validateMahasiswaNama(state.nama),
        nim = validateMahasiswaNim(state.nim),
        jurusan = validateMahasiswaJurusan(state.jurusan),
        tahunAngkatan = validateMahasiswaTahunAngkatan(state.tahunAngkatan),
        ipk = validateMahasiswaIpk(state.ipk)
    )

    return if (errors.hasErrors) ValidationResult(errors) else null
}

fun validateMahasiswaNama(value: String): String? =
    if (value.isBlank()) "Nama lengkap harus diisi!" else null

fun validateMahasiswaNim(value: String): String? =
    when {
        value.isBlank() -> "NIM harus diisi!"
        value.toLongOrNull() == null -> "NIM harus berupa angka!"
        else -> null
    }

fun validateMahasiswaJurusan(value: String): String? =
    if (value.isBlank()) "Program studi harus diisi!" else null

fun validateMahasiswaTahunAngkatan(value: String): String? =
    when {
        value.isBlank() -> "Tahun angkatan harus diisi!"
        value.toIntOrNull() == null -> "Tahun angkatan harus berupa angka!"
        else -> null
    }

fun validateMahasiswaIpk(value: String): String? {
    if (value.isBlank()) {
        return "IPK harus diisi!"
    }
    val ipkValue = value.toDoubleOrNull() ?: return "IPK harus berupa angka!"
    return if (ipkValue < 0.0 || ipkValue > 4.0) {
        "IPK harus berada di antara 0.0 hingga 4.0!"
    } else {
        null
    }
}

fun mapErrorToFieldErrors(message: String): FieldErrors {
    val lowerMessage = message.lowercase()
    var errors = FieldErrors()
    if ("nim" in lowerMessage) {
        errors = errors.copy(nim = message)
    }
    if ("nama" in lowerMessage) {
        errors = errors.copy(nama = message)
    }
    if ("jurusan" in lowerMessage || "program studi" in lowerMessage) {
        errors = errors.copy(jurusan = message)
    }
    if ("tahun" in lowerMessage) {
        errors = errors.copy(tahunAngkatan = message)
    }
    if ("ipk" in lowerMessage) {
        errors = errors.copy(ipk = message)
    }
    return errors
}
