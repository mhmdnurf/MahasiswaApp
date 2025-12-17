package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.CreateMahasiswaRequest
import com.example.mahasiswaapp.model.Mahasiswa
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MahasiswaEditUiState(
    val formState: MahasiswaFormUiState = MahasiswaFormUiState(),
    val isLoading: Boolean = true,
    val loadError: String? = null
)

class MahasiswaEditViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = Repository()

    private val nim: String? = savedStateHandle["nim"]
    private val _uiState = MutableStateFlow(
        MahasiswaEditUiState(
            isLoading = true,
            loadError = null
        )
    )
    val uiState: StateFlow<MahasiswaEditUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        if (nim.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loadError = "NIM mahasiswa tidak ditemukan"
                )
            }
        } else {
            loadMahasiswa()
        }
    }

    fun refresh() {
        if (!nim.isNullOrBlank()) loadMahasiswa()
    }

    fun updateNama(value: String) = updateFormState {
        copy(
            nama = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(nama = validateMahasiswaNama(value))
        )
    }

    fun updateNim(value: String) = updateFormState {
        copy(
            nim = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(nim = validateMahasiswaNim(value))
        )
    }

    fun updateJurusan(value: String) = updateFormState {
        copy(
            jurusan = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(jurusan = validateMahasiswaJurusan(value))
        )
    }

    fun updateTahunAngkatan(value: String) = updateFormState {
        copy(
            tahunAngkatan = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(tahunAngkatan = validateMahasiswaTahunAngkatan(value))
        )
    }

    fun updateIpk(value: String) = updateFormState {
        copy(
            ipk = value,
            generalError = null,
            fieldErrors = fieldErrors.copy(ipk = validateMahasiswaIpk(value))
        )
    }

    private fun loadMahasiswa() {
        val nimLocal = nim ?: return
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getMahasiswaDetail(nimLocal).collect { result ->
                when (result) {
                    Result.Loading -> _uiState.update { it.copy(isLoading = true, loadError = null) }
                    is Result.Error -> _uiState.update {
                        it.copy(isLoading = false, loadError = result.exception.message ?: "Gagal memuat data")
                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loadError = null,
                                formState = it.formState.copy(
                                    nama = result.data.nama_lengkap.orEmpty(),
                                    nim = result.data.nim.orEmpty(),
                                    jurusan = result.data.jurusan.orEmpty(),
                                    tahunAngkatan = result.data.tahun_angkatan?.toString().orEmpty(),
                                    ipk = result.data.ipk?.toString().orEmpty(),
                                    fieldErrors = FieldErrors(),
                                    generalError = null,
                                    isSubmitting = false,
                                    isSuccess = false
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun submit() {
        val nimLocal = nim ?: return
        val form = _uiState.value.formState
        if (!form.isFormValid || form.isSubmitting) return

        val validationResult = validateMahasiswaForm(form)
        if (validationResult != null) {
            updateFormState {
                copy(
                    fieldErrors = validationResult.fieldErrors,
                    generalError = validationResult.generalError
                )
            }
            return
        }

        val request = CreateMahasiswaRequest(
            namaLengkap = form.nama,
            nim = form.nim,
            jurusan = form.jurusan,
            tahunAngkatan = form.tahunAngkatan.toIntOrNull(),
            ipk = form.ipk.toDoubleOrNull()
        )

        viewModelScope.launch {
            updateFormState {
                copy(
                    isSubmitting = true,
                    generalError = null,
                    fieldErrors = FieldErrors(),
                    isSuccess = false
                )
            }
            when (val result = repository.updateMahasiswa(nimLocal, request)) {
                is Result.Success -> {
                    updateFormState { copy(isSubmitting = false, isSuccess = true) }
                }
                is Result.Error -> {
                    val message = result.exception.message ?: "Gagal memperbarui data"
                    val fieldErrors = mapErrorToFieldErrors(message)
                    updateFormState {
                        copy(
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
        updateFormState { copy(isSuccess = false) }
    }

    private fun updateFormState(transform: MahasiswaFormUiState.() -> MahasiswaFormUiState) {
        _uiState.update { current ->
            current.copy(
                formState = current.formState.transform()
            )
        }
    }
}
