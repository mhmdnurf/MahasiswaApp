package com.example.mahasiswaapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.CreateDosenRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DosenFormUiState(
    val nama: String = "",
    val nidn: String = "",
    val email: String = "",
    val prodi: String = "",
    val konsentrasi: String = "",
    val namaError: String? = null,
    val nidnError: String? = null,
    val emailError: String? = null,
    val prodiError: String? = null,
    val konsentrasiError: String? = null,
    val isSubmitting: Boolean = false,
    val globalError: String? = null,
    val isSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = namaError == null && nidnError == null && emailError == null &&
            prodiError == null && konsentrasiError == null &&
            nama.isNotBlank() && nidn.isNotBlank() && email.isNotBlank() &&
            prodi.isNotBlank() && konsentrasi.isNotBlank()
}

class DosenFormViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DosenFormUiState())
    val uiState: StateFlow<DosenFormUiState> = _uiState.asStateFlow()

    fun updateNama(value: String) = updateState {
        copy(nama = value, namaError = validateNama(value), globalError = null)
    }

    fun updateNidn(value: String) = updateState {
        copy(nidn = value, nidnError = validateNidn(value), globalError = null)
    }

    fun updateEmail(value: String) = updateState {
        copy(email = value, emailError = validateEmail(value), globalError = null)
    }

    fun updateProdi(value: String) = updateState {
        copy(prodi = value, prodiError = validateProdi(value), globalError = null)
    }

    fun updateKonsentrasi(value: String) = updateState {
        copy(konsentrasi = value, konsentrasiError = validateKonsentrasi(value), globalError = null)
    }

    fun submit() {
        val current = _uiState.value
        val namaError = validateNama(current.nama)
        val nidnError = validateNidn(current.nidn)
        val emailError = validateEmail(current.email)
        val prodiError = validateProdi(current.prodi)
        val konsentrasiError = validateKonsentrasi(current.konsentrasi)

        if (listOf(namaError, nidnError, emailError, prodiError, konsentrasiError).any { it != null }) {
            _uiState.update {
                it.copy(
                    namaError = namaError,
                    nidnError = nidnError,
                    emailError = emailError,
                    prodiError = prodiError,
                    konsentrasiError = konsentrasiError
                )
            }
            return
        }

        val request = CreateDosenRequest(
            nama = current.nama.trim(),
            nidn = current.nidn.trim(),
            email = current.email.trim(),
            prodi = current.prodi.trim(),
            konsentrasi = current.konsentrasi.trim()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, globalError = null) }
            when (val result = repository.createDosen(request)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            isSuccess = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            globalError = result.exception.message ?: "Gagal menyimpan data"
                        )
                    }
                }
                Result.Loading -> Unit
            }
        }
    }

    fun onSuccessHandled() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun resetGlobalError() {
        _uiState.update { it.copy(globalError = null) }
    }

    protected fun updateState(transform: DosenFormUiState.() -> DosenFormUiState) {
        _uiState.update(transform)
    }

    companion object {
        fun validateNama(value: String): String? =
            if (value.isBlank()) "Nama dosen wajib diisi" else null

        fun validateNidn(value: String): String? =
            when {
                value.isBlank() -> "NIDN wajib diisi"
                value.length < 8 -> "NIDN minimal 8 digit"
                value.any { !it.isDigit() } -> "NIDN hanya boleh angka"
                else -> null
            }

        fun validateEmail(value: String): String? =
            when {
                value.isBlank() -> "Email wajib diisi"
                !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Format email tidak valid"
                else -> null
            }

        fun validateProdi(value: String): String? =
            if (value.isBlank()) "Program studi wajib diisi" else null

        fun validateKonsentrasi(value: String): String? =
            if (value.isBlank()) "Konsentrasi wajib diisi" else null
    }
}
