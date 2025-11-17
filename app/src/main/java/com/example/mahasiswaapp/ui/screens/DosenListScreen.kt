package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Dosen
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.DosenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenListScreen(
    onNavigateBack: () -> Unit = {},
    onAddDosenClick: () -> Unit = {},
    onEditDosen: (String) -> Unit = {},
    shouldRefresh: Boolean = false,
    onRefreshConsumed: () -> Unit = {},
    snackbarMessage: String? = null,
    onSnackbarShown: () -> Unit = {},
    viewModel: DosenViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var dosenToDelete by remember { mutableStateOf<Dosen?>(null) }

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
        when (val current = deleteState) {
            is Result.Success -> {
                snackbarHostState.showSnackbar("Dosen berhasil dihapus")
                viewModel.resetDeleteState()
                viewModel.refresh()
            }
            is Result.Error -> {
                snackbarHostState.showSnackbar(current.exception.message ?: "Gagal menghapus dosen")
                viewModel.resetDeleteState()
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Data Dosen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDosenClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Dosen"
                )
            }
        }
    ) { innerPadding ->
        when (val uiState = state) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        androidx.compose.material3.CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Memuat data dosen...")
                    }
                }
            }

            is Result.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gagal memuat data",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = uiState.exception.message ?: "Terjadi kesalahan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.refresh() }) {
                        Text("Coba Lagi")
                    }
                }
            }

            is Result.Success -> {
                val data = uiState.data
                if (data.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Belum ada data dosen",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Tambahkan data untuk mulai mengelola dosen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(data, key = { it.nidn ?: it.hashCode() }) { dosen ->
                            DosenCard(
                                dosen = dosen,
                                onEditClick = {
                                    dosen.nidn?.let(onEditDosen)
                                },
                                onDeleteClick = { dosenToDelete = dosen }
                            )
                        }
                    }
                }
            }
        }
    }

    dosenToDelete?.let { target ->
        DeleteDosenDialog(
            dosen = target,
            isLoading = deleteState is Result.Loading,
            onDismiss = { dosenToDelete = null },
            onConfirm = {
                target.nidn?.let { viewModel.deleteDosen(it) }
            }
        )
    }
}

@Composable
private fun DosenCard(
    dosen: Dosen,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = dosen.nama ?: "Nama tidak tersedia",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "NIDN ${dosen.nidn ?: "-"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = dosen.email ?: "Email tidak tersedia",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dosen.nomorHp ?: "Nomor HP tidak tersedia",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Hapus")
                }
            }
        }
    }
}

@Composable
private fun DeleteDosenDialog(
    dosen: Dosen,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Hapus Dosen") },
        text = {
            Column {
                Text("Apakah Anda yakin ingin menghapus ${dosen.nama ?: "dosen ini"}?")
                if (isLoading) {
                    Spacer(Modifier.height(12.dp))
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Batal")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DosenCardPreview() {
    MahasiswaAppTheme {
        DosenCard(
            dosen = Dosen(
                nama = "Dr. Andi Pratama",
                nidn = "12345678",
                email = "andi@kampus.ac.id",
                nomorHp = "081234567890"
            ),
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
