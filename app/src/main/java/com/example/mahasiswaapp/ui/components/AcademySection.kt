package com.example.mahasiswaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.ui.theme.Blue500

data class AkademikCard(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String? = null,
    val type: String,
    val accentColor: Color = Blue500
)

private val akademikCards = listOf(
    AkademikCard(
        id = "mhs",
        title = "Daftar Mahasiswa",
        subtitle = "Data Seluruh Mahasiswa",
        badge = "X data",
        type = "mahasiswa",
        accentColor = Blue500
    ),
)

@Composable
private fun AkademikMenuChips(
    selected: String,
    onSelected: (String) -> Unit
) {
    val items = listOf("All", "Mahasiswa", "Dosen", "Mata Kuliah")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { label ->
            val isSelected = label == selected
            FilterChip(
                selected = isSelected,
                onClick = { onSelected(label) },
                label = {
                    Text(
                        label,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
private fun AkademikCardItem(
    item: AkademikCard,
    onClick: (AkademikCard) -> Unit
) {
    Card(
        onClick = { onClick(item) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(120.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                item.accentColor.copy(alpha = 0.8f),
                                item.accentColor.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(
                            topEnd = 4.dp,
                            bottomEnd = 4.dp
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            item.accentColor.copy(alpha = 0.15f),
                                            item.accentColor.copy(alpha = 0.08f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val icon = when (item.type) {
                                "mahasiswa" -> Icons.Outlined.Person
                                "dosen" -> Icons.Outlined.Menu
                                else -> Icons.Outlined.Info
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = item.title,
                                tint = item.accentColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Navigate",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "X menit lalu",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    if (item.badge != null) {
                        Surface(
                            color = item.accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                        ) {
                            Text(
                                text = item.badge,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = item.accentColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AkademikSection(
    onCardClick: (AkademikCard) -> Unit = {}
) {
    var selected by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Menu Akademik",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Kelola data akademik Anda",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(
                onClick = { /* TODO: Lihat semua */ },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Lihat semua",
                    fontWeight = FontWeight.Medium
                )
            }
        }

        AkademikMenuChips(
            selected = selected,
            onSelected = { selected = it }
        )

        Spacer(Modifier.height(8.dp))

        val filtered = remember(selected) {
            when (selected) {
                "Mahasiswa" -> akademikCards.filter { it.type == "mahasiswa" }
                "Dosen" -> akademikCards.filter { it.type == "dosen" }
                "Mata Kuliah" -> akademikCards.filter { it.type == "matakuliah" }
                else -> akademikCards
            }
        }

        filtered.forEach { item ->
            AkademikCardItem(item = item, onClick = onCardClick)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AkademikSectionPreview() {
    MahasiswaAppTheme {
        AkademikSection()
    }
}