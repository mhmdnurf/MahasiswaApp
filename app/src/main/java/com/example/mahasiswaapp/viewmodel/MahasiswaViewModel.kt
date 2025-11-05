package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Mahasiswa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MahasiswaViewModel(private val repository: Repository = Repository()) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<List<Mahasiswa>>>(Result.Loading)
    val uiState: StateFlow<Result<List<Mahasiswa>>> = _uiState.asStateFlow()

    init {
        loadMahasiswa()
    }

    fun loadMahasiswa() {
        viewModelScope.launch {
            repository.getMahasiswa().collect { result ->
                _uiState.value = result
            }
        }
    }

    fun refresh() {
        loadMahasiswa()
    }
}
