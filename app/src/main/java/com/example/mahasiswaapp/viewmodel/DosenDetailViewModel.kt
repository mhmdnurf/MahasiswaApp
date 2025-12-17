package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Dosen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DosenDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository: Repository = Repository()

    private val nidn: String? = savedStateHandle["nidn"]

    private val _uiState = MutableStateFlow<Result<Dosen>>(Result.Loading)
    val uiState: StateFlow<Result<Dosen>> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)
    val deleteState: StateFlow<Result<Unit>?> = _deleteState.asStateFlow()

    init {
        if (nidn.isNullOrBlank()) {
            _uiState.value = Result.Error(IllegalArgumentException("NIDN dosen tidak ditemukan"))
        } else {
            loadDosenDetail()
        }
    }

    fun refresh() {
        if (!nidn.isNullOrBlank()) {
            loadDosenDetail()
        }
    }

    private fun loadDosenDetail() {
        val nidnLocal = nidn ?: return
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getDosenDetail(nidnLocal).collect { result ->
                _uiState.value = result
            }
        }
    }

    fun deleteDosen() {
        val nidnLocal = nidn ?: return
        viewModelScope.launch {
            _deleteState.value = Result.Loading
            _deleteState.value = repository.deleteDosen(nidnLocal)
        }
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }
}
