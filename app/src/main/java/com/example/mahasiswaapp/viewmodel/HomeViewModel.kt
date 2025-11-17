package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AkademikUiState(
    val isLoading: Boolean = true,
    val mahasiswaCount: Int? = null,
    val dosenCount: Int? = null,
    val mahasiswaUpdatedAt: Long? = null,
    val dosenUpdatedAt: Long? = null,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AkademikUiState())
    val uiState: StateFlow<AkademikUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val currentState = _uiState.value
            val errorMessages = mutableListOf<String>()

            val mahasiswaResult = repository.getMahasiswa().first { it !is Result.Loading }
            val mahasiswaSuccess = mahasiswaResult as? Result.Success
            if (mahasiswaResult is Result.Error) {
                errorMessages += mahasiswaResult.exception.message ?: "Gagal memuat mahasiswa"
            }
            val mahasiswaNow = System.currentTimeMillis()
            _uiState.value = _uiState.value.copy(
                mahasiswaCount = mahasiswaSuccess?.data?.size ?: currentState.mahasiswaCount,
                mahasiswaUpdatedAt = mahasiswaSuccess?.let { mahasiswaNow } ?: currentState.mahasiswaUpdatedAt
            )

            val dosenResult = repository.getDosen().first { it !is Result.Loading }
            val dosenSuccess = dosenResult as? Result.Success
            if (dosenResult is Result.Error) {
                errorMessages += dosenResult.exception.message ?: "Gagal memuat dosen"
            }
            val dosenNow = System.currentTimeMillis()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                dosenCount = dosenSuccess?.data?.size ?: _uiState.value.dosenCount,
                dosenUpdatedAt = dosenSuccess?.let { dosenNow } ?: _uiState.value.dosenUpdatedAt,
                errorMessage = errorMessages.takeIf { it.isNotEmpty() }?.joinToString("\n")
            )
        }
    }
}
