package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.MahasiswaFormUiState
import com.example.mahasiswaapp.viewmodel.MahasiswaFormViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaFormScreen(
    viewModel: MahasiswaFormViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
            viewModel.onSuccessHandled()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    MahasiswaFormScreenContent(
        state = uiState,
        onNavigateBack = onNavigateBack,
        onNamaChange = viewModel::updateNama,
        onNimChange = viewModel::updateNim,
        onJurusanChange = viewModel::updateJurusan,
        onTahunAngkatanChange = { value ->
            val filtered = value.filter { it.isDigit() }.take(4)
            viewModel.updateTahunAngkatan(filtered)
        },
        onIpkChange = { value ->
            val filtered = value.filter { it.isDigit() || it == '.' }.take(4)
            viewModel.updateIpk(filtered)
        },
        onSubmit = viewModel::submit,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun FormFields(
    state: MahasiswaFormUiState,
    onNamaChange: (String) -> Unit,
    onNimChange: (String) -> Unit,
    onJurusanChange: (String) -> Unit,
    onTahunAngkatanChange: (String) -> Unit,
    onIpkChange: (String) -> Unit
) {
    OutlinedTextField(
        value = state.nama,
        onValueChange = onNamaChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Nama Lengkap") }
    )
    OutlinedTextField(
        value = state.nim,
        onValueChange = onNimChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("NIM") }
    )
    OutlinedTextField(
        value = state.jurusan,
        onValueChange = onJurusanChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Program Studi") }
    )
    OutlinedTextField(
        value = state.tahunAngkatan,
        onValueChange = onTahunAngkatanChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Tahun Angkatan") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
    OutlinedTextField(
        value = state.ipk,
        onValueChange = onIpkChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("IPK") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaFormScreenContent(
    state: MahasiswaFormUiState,
    onNavigateBack: () -> Unit,
    onNamaChange: (String) -> Unit,
    onNimChange: (String) -> Unit,
    onJurusanChange: (String) -> Unit,
    onTahunAngkatanChange: (String) -> Unit,
    onIpkChange: (String) -> Unit,
    onSubmit: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tambah Mahasiswa") },
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
            FormFields(
                state = state,
                onNamaChange = onNamaChange,
                onNimChange = onNimChange,
                onJurusanChange = onJurusanChange,
                onTahunAngkatanChange = onTahunAngkatanChange,
                onIpkChange = onIpkChange
            )

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormValid && !state.isSubmitting
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
private fun MahasiswaFormScreenPreview() {
    MahasiswaAppTheme {
        MahasiswaFormScreenContent(
            state = MahasiswaFormUiState(
                nama = "Alya Putri",
                nim = "23123456",
                jurusan = "Teknik Informatika",
                tahunAngkatan = "2022",
                ipk = "3.80"
            ),
            onNavigateBack = {},
            onNamaChange = {},
            onNimChange = {},
            onJurusanChange = {},
            onTahunAngkatanChange = {},
            onIpkChange = {},
            onSubmit = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}
