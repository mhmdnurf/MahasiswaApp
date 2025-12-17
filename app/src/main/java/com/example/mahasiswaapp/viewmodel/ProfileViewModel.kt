package com.example.mahasiswaapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mahasiswaapp.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoggingOut: Boolean = false,
    val loggedOut: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun logout() {
        if (_uiState.value.isLoggingOut) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true, errorMessage = null) }
            runCatching {
                authRepository.clearSession()
            }.onSuccess {
                _uiState.update { it.copy(isLoggingOut = false, loggedOut = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoggingOut = false, errorMessage = error.message ?: "Gagal logout")
                }
            }
        }
    }

    fun consumeLogoutEvent() {
        _uiState.update { it.copy(loggedOut = false) }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val appContext = context.applicationContext
                    return ProfileViewModel(AuthRepository(appContext)) as T
                }
            }
        }
    }
}
