package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.viewmodel.DosenEditViewModel

@Composable
fun DosenEditScreen(
    onNavigateBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: DosenEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.formState.isSuccess) {
        if (uiState.formState.isSuccess) {
            onSuccess()
            viewModel.onSuccessHandled()
        }
    }

    LaunchedEffect(uiState.formState.globalError) {
        uiState.formState.globalError?.let { snackbarHostState.showSnackbar(it) }
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Memuat data dosen...")
            }
        }

        uiState.loadError != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gagal memuat data",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.loadError.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = viewModel::refresh) {
                    Text("Coba Lagi")
                }
            }
        }

        else -> {
            DosenFormScreenContent(
                state = uiState.formState,
                onNavigateBack = onNavigateBack,
                onNamaChange = viewModel::updateNama,
                onNidnChange = viewModel::updateNidn,
                onEmailChange = viewModel::updateEmail,
                onNomorHpChange = viewModel::updateNomorHp,
                onSubmit = viewModel::submit,
                snackbarHostState = snackbarHostState,
                title = "Edit Dosen"
            )
        }
    }
}
