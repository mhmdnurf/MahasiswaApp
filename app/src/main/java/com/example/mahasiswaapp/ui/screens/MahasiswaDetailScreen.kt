package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Mahasiswa
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.MahasiswaDetailViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaDetailScreen(
    viewModel: MahasiswaDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onEditMahasiswa: (String) -> Unit = {},
    onDeleteSuccess: () -> Unit = {},
    shouldRefresh: Boolean = false,
    onRefreshConsumed: () -> Unit = {},
    snackbarMessage: String? = null,
    onSnackbarShown: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentNim = (uiState as? Result.Success)?.data?.nim
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.refresh()
            onRefreshConsumed()
        }
    }

    LaunchedEffect(snackbarMessage) {
        if (!snackbarMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(snackbarMessage)
            onSnackbarShown()
        }
    }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is Result.Success -> {
                snackbarHostState.showSnackbar("Mahasiswa dihapus")
                viewModel.resetDeleteState()
                onDeleteSuccess()
            }
            is Result.Error -> {
                val message = (deleteState as Result.Error).exception.message ?: "Gagal menghapus data"
                snackbarHostState.showSnackbar(message)
                viewModel.resetDeleteState()
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detail Mahasiswa") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { if (currentNim != null) showDeleteDialog = true },
                        enabled = currentNim != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus"
                        )
                    }
                    IconButton(
                        onClick = { currentNim?.let(onEditMahasiswa) },
                        enabled = currentNim != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(onClick = viewModel::refresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Muat ulang"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.size(12.dp))
                        Text(text = "Memuat detail mahasiswa...")
                    }
                }
            }

            is Result.Error -> {
                DetailErrorState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp),
                    message = state.exception.message ?: "Terjadi kesalahan",
                    onRetry = viewModel::refresh
                )
            }

            is Result.Success -> {
                MahasiswaDetailContent(
                    mahasiswa = state.data,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                if (deleteState !is Result.Loading) {
                    showDeleteDialog = false
                }
            },
            title = { Text("Hapus Mahasiswa") },
            text = {
                Column {
                    Text("Apakah Anda yakin ingin menghapus data mahasiswa ini?")
                    if (deleteState is Result.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteMahasiswa() },
                    enabled = deleteState !is Result.Loading
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    enabled = deleteState !is Result.Loading
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun DetailErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Gagal memuat data",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(16.dp))
        Button(onClick = onRetry) {
            Text("Coba Lagi")
        }
    }
}

@Composable
private fun MahasiswaDetailContent(
    mahasiswa: Mahasiswa,
    modifier: Modifier = Modifier
) {
    val initials = mahasiswa.nama_lengkap
        ?.split(" ")
        ?.take(2)
        ?.joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
        ?.ifBlank { "?" }
        ?: "?"

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.size(16.dp))
                Text(
                    text = mahasiswa.nama_lengkap ?: "Nama tidak tersedia",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "NIM ${mahasiswa.nim ?: "-"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(label = "Program Studi", value = mahasiswa.jurusan ?: "-")
                InfoRow(label = "Tahun Angkatan", value = mahasiswa.tahun_angkatan?.toString() ?: "-")
                InfoRow(label = "IPK", value = mahasiswa.ipk?.let { String.format(Locale.US, "%.2f", it) } ?: "-")
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MahasiswaDetailContentPreview() {
    MahasiswaAppTheme {
        MahasiswaDetailContent(
            mahasiswa = Mahasiswa(
                nama_lengkap = "Alya Putri",
                nim = "23123456",
                jurusan = "Teknik Informatika",
                tahun_angkatan = 2022,
                ipk = 3.84
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
