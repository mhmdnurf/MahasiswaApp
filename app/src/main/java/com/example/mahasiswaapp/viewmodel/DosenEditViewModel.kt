package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.CreateDosenRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DosenEditUiState(
    val formState: DosenFormUiState = DosenFormUiState(),
    val isLoading: Boolean = true,
    val loadError: String? = null
)

class DosenEditViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = Repository()
    private val nidn: String? = savedStateHandle["nidn"]

    private val _uiState = MutableStateFlow(DosenEditUiState())
    val uiState: StateFlow<DosenEditUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        if (nidn.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loadError = "NIDN dosen tidak ditemukan"
                )
            }
        } else {
            loadDosen()
        }
    }

    fun refresh() {
        if (!nidn.isNullOrBlank()) {
            loadDosen()
        }
    }

    private fun loadDosen() {
        val nidnLocal = nidn ?: return
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getDosenDetail(nidnLocal).collect { result ->
                when (result) {
                    Result.Loading -> _uiState.update { it.copy(isLoading = true, loadError = null) }
                    is Result.Error -> _uiState.update {
                        it.copy(isLoading = false, loadError = result.exception.message ?: "Gagal memuat data")
                    }
                    is Result.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            formState = it.formState.copy(
                                nama = result.data.nama.orEmpty(),
                                nidn = result.data.nidn.orEmpty(),
                                email = result.data.email.orEmpty(),
                                nomorHp = result.data.nomorHp.orEmpty(),
                                namaError = null,
                                nidnError = null,
                                emailError = null,
                                nomorHpError = null,
                                isSubmitting = false,
                                isSuccess = false,
                                globalError = null
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateNama(value: String) = updateForm {
        copy(nama = value, namaError = DosenFormViewModel.validateNama(value), globalError = null)
    }
    fun updateNidn(value: String) = updateForm {
        copy(nidn = value, nidnError = DosenFormViewModel.validateNidn(value), globalError = null)
    }
    fun updateEmail(value: String) = updateForm {
        copy(email = value, emailError = DosenFormViewModel.validateEmail(value), globalError = null)
    }
    fun updateNomorHp(value: String) = updateForm {
        copy(nomorHp = value, nomorHpError = DosenFormViewModel.validateNomorHp(value), globalError = null)
    }

    fun submit() {
        val nidnLocal = nidn ?: return
        val form = _uiState.value.formState
        val namaError = DosenFormViewModel.validateNama(form.nama)
        val nidnError = DosenFormViewModel.validateNidn(form.nidn)
        val emailError = DosenFormViewModel.validateEmail(form.email)
        val nomorHpError = DosenFormViewModel.validateNomorHp(form.nomorHp)

        if (listOf(namaError, nidnError, emailError, nomorHpError).any { it != null }) {
            updateForm {
                copy(
                    namaError = namaError,
                    nidnError = nidnError,
                    emailError = emailError,
                    nomorHpError = nomorHpError
                )
            }
            return
        }

        val request = CreateDosenRequest(
            nama = form.nama.trim(),
            nidn = form.nidn.trim(),
            email = form.email.trim(),
            nomorHp = form.nomorHp.trim()
        )

        viewModelScope.launch {
            updateForm { copy(isSubmitting = true, globalError = null) }
            when (val result = repository.updateDosen(nidnLocal, request)) {
                is Result.Success -> updateForm { copy(isSubmitting = false, isSuccess = true) }
                is Result.Error -> updateForm {
                    copy(
                        isSubmitting = false,
                        globalError = result.exception.message ?: "Gagal memperbarui data"
                    )
                }
                Result.Loading -> Unit
            }
        }
    }

    fun onSuccessHandled() {
        updateForm { copy(isSuccess = false) }
    }

    fun resetGlobalError() {
        updateForm { copy(globalError = null) }
    }

    private fun updateForm(transform: DosenFormUiState.() -> DosenFormUiState) {
        _uiState.update { current ->
            current.copy(formState = current.formState.transform())
        }
    }
}
