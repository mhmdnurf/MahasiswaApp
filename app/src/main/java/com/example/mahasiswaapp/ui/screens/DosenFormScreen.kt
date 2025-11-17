package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.DosenFormUiState
import com.example.mahasiswaapp.viewmodel.DosenFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenFormScreen(
    onNavigateBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: DosenFormViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSuccess()
            viewModel.onSuccessHandled()
        }
    }

    LaunchedEffect(state.globalError) {
        state.globalError?.let { snackbarHostState.showSnackbar(it) }
    }

    DosenFormScreenContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onNamaChange = viewModel::updateNama,
        onNidnChange = viewModel::updateNidn,
        onEmailChange = viewModel::updateEmail,
        onNomorHpChange = viewModel::updateNomorHp,
        onSubmit = viewModel::submit,
        snackbarHostState = snackbarHostState,
        title = "Tambah Dosen"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenFormScreenContent(
    state: DosenFormUiState,
    onNavigateBack: () -> Unit,
    onNamaChange: (String) -> Unit,
    onNidnChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNomorHpChange: (String) -> Unit,
    onSubmit: () -> Unit,
    snackbarHostState: SnackbarHostState,
    title: String
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nama,
                onValueChange = onNamaChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nama Dosen") },
                isError = state.namaError != null,
                supportingText = {
                    state.namaError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            OutlinedTextField(
                value = state.nidn,
                onValueChange = onNidnChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("NIDN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.nidnError != null,
                supportingText = {
                    state.nidnError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.emailError != null,
                supportingText = {
                    state.emailError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            OutlinedTextField(
                value = state.nomorHp,
                onValueChange = onNomorHpChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nomor HP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = state.nomorHpError != null,
                supportingText = {
                    state.nomorHpError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(vertical = 4.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Simpan")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DosenFormScreenPreview() {
    MahasiswaAppTheme {
        DosenFormScreenContent(
            state = DosenFormUiState(
                nama = "Dr. Andi",
                nidn = "12345678",
                email = "andi@kampus.ac.id",
                nomorHp = "08123456789"
            ),
            onNavigateBack = {},
            onNamaChange = {},
            onNidnChange = {},
            onEmailChange = {},
            onNomorHpChange = {},
            onSubmit = {},
            snackbarHostState = SnackbarHostState(),
            title = "Tambah Dosen"
        )
    }
}
