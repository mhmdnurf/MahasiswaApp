package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.FieldErrors
import com.example.mahasiswaapp.viewmodel.MahasiswaFormUiState
import com.example.mahasiswaapp.viewmodel.MahasiswaFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaFormScreen(
    viewModel: MahasiswaFormViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
            viewModel.onSuccessHandled()
        }
    }

    MahasiswaFormScreenContent(
        state = uiState,
        onNavigateBack = onNavigateBack,
        onNamaChange = viewModel::updateNama,
        onNimChange = viewModel::updateNim,
        onJurusanChange = viewModel::updateJurusan,
        onTahunAngkatanChange = { value: String ->
            val filtered = value.filter { it.isDigit() }.take(4)
            viewModel.updateTahunAngkatan(filtered)
        },
        onIpkChange = { value: String ->
            val filtered = value.filter { it.isDigit() || it == '.' }.take(4)
            viewModel.updateIpk(filtered)
        },
        onSubmit = viewModel::submit
    )
}

@Composable
fun FormFields(
    state: MahasiswaFormUiState,
    errors: FieldErrors,
    onNamaChange: (String) -> Unit,
    onNimChange: (String) -> Unit,
    onJurusanChange: (String) -> Unit,
    onTahunAngkatanChange: (String) -> Unit,
    onIpkChange: (String) -> Unit
) {
    val programStudiOptions = listOf("Teknik Informatika", "Sistem Informasi", "Komputer Akuntansi")
    var jurusanExpanded by remember { mutableStateOf(false) }
    var jurusanFieldSize by remember { mutableStateOf(Size.Zero) }
    val dropdownWidth = with(LocalDensity.current) { jurusanFieldSize.width.toDp() }
    val dropdownModifier = if (jurusanFieldSize.width == 0f) {
        Modifier
    } else {
        Modifier.width(dropdownWidth)
    }

    OutlinedTextField(
        value = state.nama,
        onValueChange = onNamaChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Nama Lengkap") },
        isError = errors.nama != null,
        supportingText = errors.nama?.let {
            { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    )
    OutlinedTextField(
        value = state.nim,
        onValueChange = onNimChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("NIM") },
        isError = errors.nim != null,
        supportingText = errors.nim?.let {
            { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { jurusanExpanded = !jurusanExpanded }
    ) {
        OutlinedTextField(
            value = state.jurusan,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    jurusanFieldSize = coordinates.size.toSize()
                },
            label = { Text("Program Studi") },
            isError = errors.jurusan != null,
            supportingText = errors.jurusan?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            trailingIcon = {
                IconButton(onClick = { jurusanExpanded = !jurusanExpanded }) {
                    val icon = if (jurusanExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                    Icon(icon, contentDescription = "Toggle dropdown")
                }
            }
        )
        DropdownMenu(
            expanded = jurusanExpanded,
            onDismissRequest = { jurusanExpanded = false },
            modifier = dropdownModifier
        ) {
            programStudiOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onJurusanChange(option)
                        jurusanExpanded = false
                    }
                )
            }
        }
    }
    OutlinedTextField(
        value = state.tahunAngkatan,
        onValueChange = onTahunAngkatanChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Tahun Angkatan") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        isError = errors.tahunAngkatan != null,
        supportingText = errors.tahunAngkatan?.let {
            { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    )
    OutlinedTextField(
        value = state.ipk,
        onValueChange = onIpkChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("IPK") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        isError = errors.ipk != null,
        supportingText = errors.ipk?.let {
            { Text(it, color = MaterialTheme.colorScheme.error) }
        }
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
    onSubmit: () -> Unit
) {
    Scaffold(
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
                errors = state.fieldErrors,
                onNamaChange = onNamaChange,
                onNimChange = onNimChange,
                onJurusanChange = onJurusanChange,
                onTahunAngkatanChange = onTahunAngkatanChange,
                onIpkChange = onIpkChange
            )

            state.generalError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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
fun MahasiswaFormScreenPreview() {
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
            onSubmit = {}
        )
    }
}
