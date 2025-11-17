package com.example.mahasiswaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mahasiswaapp.ui.theme.Blue500
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import com.example.mahasiswaapp.viewmodel.AkademikUiState
import kotlin.math.floor

enum class AkademikCardType {
    MAHASISWA,
    DOSEN
}

data class AkademikCard(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String? = null,
    val type: AkademikCardType,
    val accentColor: Color = Blue500,
    val lastUpdatedText: String = "-"
)

@Composable
private fun AkademikMenuChips(
    selected: String,
    onSelected: (String) -> Unit
) {
    val items = listOf("All", "Mahasiswa", "Dosen")
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
                        text = label,
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
                                AkademikCardType.MAHASISWA -> Icons.Outlined.Person
                                AkademikCardType.DOSEN -> Icons.Outlined.Info
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = item.title,
                                tint = item.accentColor
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Text(
                            text = item.lastUpdatedText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    if (item.badge != null) {
                        Surface(
                            color = item.accentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
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
    state: AkademikUiState,
    onCardClick: (AkademikCardType) -> Unit = {},
    onRefreshClick: () -> Unit = {}
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
                onClick = onRefreshClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    if (state.isLoading) "Memuat..." else "Muat ulang",
                    fontWeight = FontWeight.Medium
                )
            }
        }

        state.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
        }

        AkademikMenuChips(
            selected = selected,
            onSelected = { selected = it }
        )

        Spacer(Modifier.height(8.dp))

        val cards = listOf(
            AkademikCard(
                id = "mhs",
                title = "Daftar Mahasiswa",
                subtitle = "Data seluruh mahasiswa",
                badge = state.mahasiswaCount?.let { "$it data" } ?: "– data",
                type = AkademikCardType.MAHASISWA,
                accentColor = Blue500,
                lastUpdatedText = formatLastUpdated(state.mahasiswaUpdatedAt)
            ),
            AkademikCard(
                id = "dsn",
                title = "Daftar Dosen",
                subtitle = "Data seluruh dosen",
                badge = state.dosenCount?.let { "$it data" } ?: "– data",
                type = AkademikCardType.DOSEN,
                accentColor = MaterialTheme.colorScheme.secondary,
                lastUpdatedText = formatLastUpdated(state.dosenUpdatedAt)
            )
        )

        val filtered = when (selected) {
            "Mahasiswa" -> cards.filter { it.type == AkademikCardType.MAHASISWA }
            "Dosen" -> cards.filter { it.type == AkademikCardType.DOSEN }
            else -> cards
        }

        filtered.forEach { item ->
            AkademikCardItem(item = item) { card -> onCardClick(card.type) }
        }
    }
}

private fun formatLastUpdated(timestamp: Long?): String {
    timestamp ?: return "Belum ada data"
    val diff = System.currentTimeMillis() - timestamp
    if (diff < 60_000L) return "Baru saja"
    val minutes = diff / 60_000L
    if (minutes < 60) return "$minutes menit lalu"
    val hours = minutes / 60.0
    if (hours < 24) {
        val rounded = floor(hours).toInt()
        return "$rounded jam lalu"
    }
    val days = minutes / (60 * 24)
    return "$days hari lalu"
}

@Preview(showBackground = true)
@Composable
fun AkademikSectionPreview() {
    MahasiswaAppTheme {
        AkademikSection(
            state = AkademikUiState(
                isLoading = false,
                mahasiswaCount = 120,
                dosenCount = 45,
                mahasiswaUpdatedAt = System.currentTimeMillis() - 15 * 60 * 1000,
                dosenUpdatedAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000
            )
        )
    }
}
