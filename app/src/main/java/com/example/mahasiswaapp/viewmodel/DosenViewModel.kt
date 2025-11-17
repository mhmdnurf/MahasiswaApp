package com.example.mahasiswaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mahasiswaapp.data.Repository
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Dosen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DosenViewModel(
    private val repository: Repository = Repository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<List<Dosen>>>(Result.Loading)
    val uiState: StateFlow<Result<List<Dosen>>> = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)
    val deleteState: StateFlow<Result<Unit>?> = _deleteState.asStateFlow()

    init {
        loadDosen()
    }

    fun loadDosen() {
        viewModelScope.launch {
            repository.getDosen().collect { result ->
                _uiState.value = result
            }
        }
    }

    fun refresh() {
        loadDosen()
    }

    fun deleteDosen(nidn: String) {
        viewModelScope.launch {
            _deleteState.value = Result.Loading
            _deleteState.value = repository.deleteDosen(nidn)
        }
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }
}
