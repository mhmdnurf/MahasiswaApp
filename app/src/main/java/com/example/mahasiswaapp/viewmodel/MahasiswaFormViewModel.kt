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
    val fieldErrors: FieldErrors = FieldErrors(),
    val generalError: String? = null,
    val isSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = !fieldErrors.hasErrors &&
            nama.isNotBlank() &&
            nim.isNotBlank() &&
            jurusan.isNotBlank() &&
            tahunAngkatan.isNotBlank() &&
            ipk.isNotBlank()
}

class MahasiswaFormViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MahasiswaFormUiState())
    val uiState: StateFlow<MahasiswaFormUiState> = _uiState.asStateFlow()

    fun updateNama(value: String) = updateState {
        copy(
            nama = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(nama = validateMahasiswaNama(value))
        )
    }

    fun updateNim(value: String) = updateState {
        copy(
            nim = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(nim = validateMahasiswaNim(value))
        )
    }

    fun updateJurusan(value: String) = updateState {
        copy(
            jurusan = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(jurusan = validateMahasiswaJurusan(value))
        )
    }

    fun updateTahunAngkatan(value: String) = updateState {
        copy(
            tahunAngkatan = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(tahunAngkatan = validateMahasiswaTahunAngkatan(value))
        )
    }

    fun updateIpk(value: String) = updateState {
        copy(
            ipk = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(ipk = validateMahasiswaIpk(value))
        )
    }

    fun submit() {
        val state = _uiState.value
        if (!state.isFormValid || state.isSubmitting) return

        val validationResult = validateMahasiswaForm(state)
        if (validationResult != null) {
            _uiState.update {
                it.copy(
                    fieldErrors = validationResult.fieldErrors,
                    generalError = validationResult.generalError
                )
            }
            return
        }

        val request = CreateMahasiswaRequest(
            namaLengkap = state.nama,
            nim = state.nim,
            jurusan = state.jurusan,
            tahunAngkatan = state.tahunAngkatan.toIntOrNull(),
            ipk = state.ipk.toDoubleOrNull()
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    generalError = null,
                    fieldErrors = FieldErrors(),
                    isSuccess = false
                )
            }
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
                    val message = result.exception.message ?: "Gagal menyimpan data"
                    val fieldErrors = mapErrorToFieldErrors(message)
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            generalError = if (fieldErrors.hasErrors) null else message,
                            fieldErrors = fieldErrors
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
