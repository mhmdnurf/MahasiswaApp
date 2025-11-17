package com.example.mahasiswaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mahasiswaapp.ui.components.AkademikCardType
import com.example.mahasiswaapp.ui.components.AkademikSection
import com.example.mahasiswaapp.ui.components.GreetingHeader
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.AkademikUiState
import com.example.mahasiswaapp.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onMahasiswaListClick: () -> Unit,
    onDosenListClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    HomeScreenContent(
        state = state,
        onMahasiswaListClick = onMahasiswaListClick,
        onDosenListClick = onDosenListClick,
        onRefreshClick = { viewModel.refresh() },
        onProfileClick = onProfileClick
    )
}

@Composable
private fun HomeScreenContent(
    state: AkademikUiState,
    onMahasiswaListClick: () -> Unit,
    onDosenListClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 60.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        GreetingHeader(
            onProfileClick = onProfileClick
        )
        AkademikSection(
            state = state,
            onCardClick = { cardType ->
                when (cardType) {
                    AkademikCardType.MAHASISWA -> onMahasiswaListClick()
                    AkademikCardType.DOSEN -> onDosenListClick()
                }
            },
            onRefreshClick = onRefreshClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MahasiswaAppTheme {
        HomeScreenContent(
            state = AkademikUiState(
                isLoading = false,
                mahasiswaCount = 120,
                dosenCount = 45,
                mahasiswaUpdatedAt = System.currentTimeMillis(),
                dosenUpdatedAt = System.currentTimeMillis()
            ),
            onMahasiswaListClick = {},
            onDosenListClick = {},
            onRefreshClick = {},
            onProfileClick = {}
        )
    }
}
