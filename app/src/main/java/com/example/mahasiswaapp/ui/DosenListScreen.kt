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
import com.example.mahasiswaapp.model.Dosen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenListScreen(
    dosens: List<Dosen>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Dosen") }
            )
        }
    ) { innerPadding ->
        if (dosens.isEmpty()) {
            // when empty, show message near top instead of centered
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(28.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Belum ada data dosen", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Tambahkan data dosen untuk mulai melihat daftar.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
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
                items(dosens, key = { it.nidn }) { dosen ->
                    DosenCard(dosen)
                }
            }
        }
    }
}

@Composable
fun DosenCard(dosen: Dosen) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val initials = dosen.nama.split(" ").take(2).joinToString("") { it.first().toString() }.uppercase()
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.secondary),
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
                    text = dosen.nama,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(text = "NIDN ${dosen.nidn}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(2.dp))
                Text(text = dosen.prodi, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(2.dp))
                AssistChip(onClick = {}, label = { Text(dosen.bidangKeahlian) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DosenListScreenPreview() {
    val sample = listOf(
        Dosen("Dr. Andi Wijaya", "1234567890", "Informatika", "Data Science"),
        Dosen("Ir. Siti Rahma, M.Kom.", "0987654321", "Sistem Informasi", "Internet of Things"),
        Dosen("Dharma Putra, M.T.", "1112223334", "Teknik Elektro", "Embedded Systems"),
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        DosenListScreen(dosens = sample)
    }
}
