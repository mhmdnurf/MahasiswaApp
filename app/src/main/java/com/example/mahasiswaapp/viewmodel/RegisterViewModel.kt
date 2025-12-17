package com.example.mahasiswaapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mahasiswaapp.data.AuthRepository
import com.example.mahasiswaapp.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val nama: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val shouldNavigate: Boolean = false
) {
    val isFormValid: Boolean
        get() = username.isNotBlank() &&
            password.length >= 6 &&
            email.contains("@") &&
            nama.isNotBlank()
}

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateUsername(value: String) {
        _uiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun updateEmail(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun updateNama(value: String) {
        _uiState.update { it.copy(nama = value, errorMessage = null) }
    }

    fun register() {
        val state = _uiState.value
        if (!state.isFormValid || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authRepository.register(state.username, state.password, state.email, state.nama)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            shouldNavigate = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Registrasi gagal",
                            shouldNavigate = false
                        )
                    }
                }
                Result.Loading -> Unit
            }
        }
    }

    fun consumeNavigation() {
        _uiState.update { it.copy(shouldNavigate = false) }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val appContext = context.applicationContext
                    return RegisterViewModel(AuthRepository(appContext)) as T
                }
            }
        }
    }
}
