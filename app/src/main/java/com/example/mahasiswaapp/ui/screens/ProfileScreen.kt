package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahasiswaapp.R
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme

data class ProfileInfo(
    val nama: String,
    val nim: String,
    val jurusan: String,
    val tanggalLahir: String = "31 Des 2003",
    val status: String = "Mahasiswa Aktif",
    val gender: String = "Laki-laki",
    val angkatan: String = "2023",
    val semesterAktif: Int = 3,
    val ipk: String = "3.85"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    profileInfo: ProfileInfo = ProfileInfo(
        nama = "Muhammad Nurfatkhur Rahman",
        nim = "1219002",
        jurusan = "Teknik Informatika"
    )
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .windowInsetsPadding(WindowInsets(0))
            ) {
                Image(
                    painter = painterResource(R.drawable.my_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.8f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
            }
        }
    }
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    Column (
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
    ){
        Spacer(modifier = Modifier.height(310.dp))
        Text(
            text = profileInfo.nama,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            ),
            color = Color.White
        )
        Text(
            text = profileInfo.nim,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )
        Text(
            text = profileInfo.jurusan,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
    }

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MahasiswaAppTheme {
        ProfileScreen()
    }
}
