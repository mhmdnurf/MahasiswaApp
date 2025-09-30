package com.example.mahasiswaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahasiswaapp.model.Mahasiswa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaListScreen(
    mahasiswas: List<Mahasiswa>,
    onMahasiswaClick: (Mahasiswa) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Mahasiswa") }
            )
        }
    ) { innerPadding ->
        if (mahasiswas.isEmpty()) {
            // ketika kosong, tunjukkan empty state di bagian atas (tidak center)
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
                // kurangi top padding agar list muncul lebih ke atas
                contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = mahasiswas,
                    key = { it.nim }
                ) { mahasiswa ->
                    MahasiswaCard(
                        mahasiswa = mahasiswa,
                        onClick = { onMahasiswaClick(mahasiswa) }
                    )
                }

                // tombol "Halaman Dosen" sebagai footer dihapus karena bottom navigation dipakai
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
            // Avatar singkatan nama (misal Alya Putri -> AP)
            val initials = mahasiswa.nama.split(" ").take(2).joinToString("") { it.first().toString() }.uppercase()
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mahasiswa.nama,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "NIM ${mahasiswa.nim}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = mahasiswa.prodi,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AssistChip(
                onClick = onClick,
                label = { Text("IPK ${formatIpk(mahasiswa.ipk)}") }
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier, // respect the provided modifier; don't force full height
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

private fun formatIpk(value: Double): String = String.format("%.2f", value)

@Preview(showBackground = true)
@Composable
private fun MahasiswaListScreenPreview() {
    val sample = listOf(
        Mahasiswa("Alya Putri", "23123456", "Informatika", 3.82),
        Mahasiswa("Budi Santoso", "23123457", "Sistem Informasi", 3.45),
        Mahasiswa("Citra Lestari", "23123458", "Teknik Industri", 3.92),
        Mahasiswa("Dian Pratama", "23123459", "Manajemen", 3.10),
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        MahasiswaListScreen(mahasiswas = sample)
    }
}

@Composable
private fun MahasiswaCardPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        MahasiswaCard(
            mahasiswa = Mahasiswa("Alya Putri", "23123456", "Informatika", 3.82)
        )
    }
}