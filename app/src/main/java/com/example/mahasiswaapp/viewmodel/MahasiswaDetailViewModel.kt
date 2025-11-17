package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Mahasiswa
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MahasiswaDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository: Repository = Repository()

    private val nim: String? = savedStateHandle["nim"]

    private val _uiState = MutableStateFlow<Result<Mahasiswa>>(Result.Loading)
    val uiState: StateFlow<Result<Mahasiswa>> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)
    val deleteState: StateFlow<Result<Unit>?> = _deleteState.asStateFlow()

    init {
        if (nim.isNullOrBlank()) {
            _uiState.value = Result.Error(IllegalArgumentException("NIM mahasiswa tidak ditemukan"))
        } else {
            loadMahasiswaDetail()
        }
    }

    fun refresh() {
        if (!nim.isNullOrBlank()) {
            loadMahasiswaDetail()
        }
    }

    private fun loadMahasiswaDetail() {
        val nimLocal = nim ?: return
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getMahasiswaDetail(nimLocal).collect { result ->
                _uiState.value = result
            }
        }
    }

    fun deleteMahasiswa() {
        val nimLocal = nim ?: return
        viewModelScope.launch {
            _deleteState.value = Result.Loading
            _deleteState.value = repository.deleteMahasiswa(nimLocal)
        }
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }
}
