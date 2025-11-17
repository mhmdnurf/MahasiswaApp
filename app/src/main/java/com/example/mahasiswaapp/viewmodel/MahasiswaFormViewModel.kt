package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.CreateMahasiswaRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MahasiswaFormUiState(
    val nama: String = "",
    val nim: String = "",
    val jurusan: String = "",
    val tahunAngkatan: String = "",
    val ipk: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = nama.isNotBlank() && nim.isNotBlank()
}

class MahasiswaFormViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MahasiswaFormUiState())
    val uiState: StateFlow<MahasiswaFormUiState> = _uiState.asStateFlow()

    fun updateNama(value: String) = updateState { copy(nama = value, errorMessage = null) }
    fun updateNim(value: String) = updateState { copy(nim = value, errorMessage = null) }
    fun updateJurusan(value: String) = updateState { copy(jurusan = value, errorMessage = null) }
    fun updateTahunAngkatan(value: String) = updateState { copy(tahunAngkatan = value, errorMessage = null) }
    fun updateIpk(value: String) = updateState { copy(ipk = value, errorMessage = null) }

    fun submit() {
        val state = _uiState.value
        if (!state.isFormValid || state.isSubmitting) return

        val request = CreateMahasiswaRequest(
            namaLengkap = state.nama,
            nim = state.nim,
            jurusan = state.jurusan.ifBlank { null },
            tahunAngkatan = state.tahunAngkatan.toIntOrNull(),
            ipk = state.ipk.toDoubleOrNull()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null, isSuccess = false) }
            when (val result = repository.createMahasiswa(request)) {
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
                            errorMessage = result.exception.message ?: "Gagal menyimpan data"
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

    private fun updateState(reducer: MahasiswaFormUiState.() -> MahasiswaFormUiState) {
        _uiState.update(reducer)
    }
}
