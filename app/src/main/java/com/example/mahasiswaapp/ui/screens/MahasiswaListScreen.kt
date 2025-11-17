package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.data.Result
import com.example.mahasiswaapp.model.Mahasiswa
import com.example.mahasiswaapp.viewmodel.MahasiswaViewModel
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaListScreen(
    mahasiswas: List<Mahasiswa>? = null,
    onMahasiswaClick: (Mahasiswa) -> Unit = {},
    viewModel: MahasiswaViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onAddMahasiswaClick: () -> Unit = {},
    shouldRefresh: Boolean = false,
    onRefreshConsumed: () -> Unit = {},
    snackbarMessage: String? = null,
    onSnackbarShown: () -> Unit = {}
) {

    val vmState by viewModel.uiState.collectAsState()
    val uiState: Result<List<Mahasiswa>> = mahasiswas?.let { Result.Success(it) } ?: vmState
    val snackbarHostState = remember { SnackbarHostState() }

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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Data Mahasiswa") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMahasiswaClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Mahasiswa"
                )
            }
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Memuat data mahasiswa...")
                    }
                }
            }

            is Result.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(28.dp))
                    ErrorState(
                        error = state.exception.message ?: "Terjadi kesalahan",
                        onRetry = { viewModel.refresh() }
                    )
                }
            }

            is Result.Success -> {
                val list = state.data
                if (list.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(28.dp))
                        EmptyState(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                        Spacer(Modifier.height(12.dp))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(list, key = { it.nim ?: it.hashCode() }) { mahasiswa ->
                            MahasiswaCard(
                                mahasiswa = mahasiswa,
                                onClick = { onMahasiswaClick(mahasiswa) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(
    mahasiswa: Mahasiswa,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val initials = mahasiswa.nama_lengkap?.split(" ")?.take(2)?.joinToString("") { it.firstOrNull()?.toString() ?: "" }?.uppercase() ?: "?"
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifBlank { "?" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mahasiswa.nama_lengkap ?: "Nama tidak tersedia",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "NIM ${mahasiswa.nim ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = mahasiswa.jurusan ?: "Jurusan tidak tersedia",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AssistChip(
                onClick = onClick,
                label = { Text("IPK ${mahasiswa.ipk?.let { formatIpk(it) } ?: "-"}") }
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Belum ada data mahasiswa",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tambahkan data untuk mulai melihat daftar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gagal memuat data",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Coba Lagi")
        }
    }
}

private fun formatIpk(value: Double): String = String.format(Locale.US, "%.2f", value)

@Preview(showBackground = true)
@Composable
private fun MahasiswaListScreenPreview() {
    val sample = listOf(
        Mahasiswa("Alya Putri", "23123456", "Informatika",2023, 3.82),
    )
    MahasiswaAppTheme {
        MahasiswaListScreen(sample)
    }
}
