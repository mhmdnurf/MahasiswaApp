package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mahasiswaapp.ui.components.AkademikCard
import com.example.mahasiswaapp.ui.components.AkademikSection
import com.example.mahasiswaapp.ui.components.GreetingHeader
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme

@Composable
fun HomeScreen(
    onMahasiswaListClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 60.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        GreetingHeader()
        AkademikSection(
            onCardClick = { card: AkademikCard ->
                if (card.type == "mahasiswa") {
                    onMahasiswaListClick()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MahasiswaAppTheme {
        HomeScreen(onMahasiswaListClick = {})
    }
}
