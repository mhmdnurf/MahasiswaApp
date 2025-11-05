package com.example.mahasiswaapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun PillsChips() {
    val items = listOf("Semua", "Perkuliahan", "Tugas", "Ujian", "Proyek")
    var selectedItem by remember { mutableStateOf(setOf<String>("Semua")) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) {label ->
            val isSelected = label in selectedItem
            FilterChip(
                selected = isSelected,
                onClick = {
                    selectedItem = if (isSelected) selectedItem - label else selectedItem + label
                },
                label = { Text(label) },
                shape = CircleShape,
            )
        }
    }
}