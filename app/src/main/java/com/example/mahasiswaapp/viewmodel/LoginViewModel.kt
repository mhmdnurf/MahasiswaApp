package com.example.mahasiswaapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mahasiswaapp.data.AuthRepository
import com.example.mahasiswaapp.data.AuthTokenProvider
import com.example.mahasiswaapp.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val shouldNavigate: Boolean = false,
    val hasSession: Boolean = false
) {
    val isFormValid: Boolean
        get() = username.isNotBlank() && password.length >= 4
}

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private var currentToken: String? = null

    init {
        preloadSession()
    }

    fun updateUsername(value: String) {
        _uiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value
        if (!state.isFormValid || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authRepository.login(state.username, state.password)) {
                is Result.Success -> {
                    currentToken = result.data.token
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            shouldNavigate = true,
                            hasSession = true
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Login gagal",
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

    private fun preloadSession() {
        viewModelScope.launch {
            authRepository.preloadToken()
            authRepository.tokenFlow.collect { token ->
                AuthTokenProvider.setToken(token)
                if (!token.isNullOrBlank() && token != currentToken) {
                    currentToken = token
                    _uiState.update {
                        it.copy(
                            shouldNavigate = true,
                            hasSession = true,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else if (token.isNullOrBlank()) {
                    currentToken = null
                    _uiState.update { it.copy(hasSession = false) }
                }
            }
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val appContext = context.applicationContext
                    return LoginViewModel(AuthRepository(appContext)) as T
                }
            }
        }
    }
}
