package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BhajanItem
import com.example.player.BhajanPlayerManager
import com.example.ui.components.DevotionalTopAppBar
import com.example.ui.components.formatSeconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhajanScreen(
    bhajans: List<BhajanItem>,
    currentBhajan: BhajanItem?,
    isPlaying: Boolean,
    currentSec: Int,
    durationSec: Int,
    isLooping: Boolean,
    onPlayBhajan: (BhajanItem) -> Unit,
    onTogglePlayPause: () -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrev: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onToggleLoop: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("सर्व") }
    var searchQuery by remember { mutableStateOf("") }
    var viewingLyricsBhajan by remember { mutableStateOf<BhajanItem?>(null) }
    var lyricsFontSize by remember { mutableStateOf(16.sp) }

    val categories = listOf("सर्व", "आरती", "स्तोत्र", "जप", "भजन")

    val filteredList = remember(selectedCategory, searchQuery, bhajans) {
        bhajans.filter { item ->
            (selectedCategory == "सर्व" || item.category == selectedCategory) &&
                    (searchQuery.isBlank() || item.title.contains(searchQuery, ignoreCase = true) ||
                            item.subtitle.contains(searchQuery, ignoreCase = true) ||
                            item.lyrics.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "🎵 भजने व आरती",
                subtitle = "श्री संत गजानन महाराज अमृतवाणी",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Search Bar & Filter Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("भजन किंवा आरती शोधा...", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFE65100))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE65100),
                        unfocusedBorderColor = Color(0xFFE0D5C7)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bhajan_search_field")
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = (selectedCategory == cat)
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = {
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE65100),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFFFFF3E0),
                                labelColor = Color(0xFF5D4037)
                            )
                        )
                    }
                }
            }

            // Bhajan / Aarti List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    val isCurrent = currentBhajan?.id == item.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                if (isCurrent) {
                                    onTogglePlayPause()
                                } else {
                                    onPlayBhajan(item)
                                }
                            }
                            .testTag("bhajan_card_${item.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isCurrent) CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE65100))
                        ) else null,
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Play/Pause Action Button
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCurrent && isPlaying) Color(0xFF880E4F) else Color(0xFFE65100)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCurrent && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isCurrent && isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = when (item.category) {
                                            "आरती" -> Color(0xFFFFEBEE)
                                            "स्तोत्र" -> Color(0xFFEDE7F6)
                                            "जप" -> Color(0xFFE8F5E9)
                                            else -> Color(0xFFFFF8E1)
                                        }
                                    ) {
                                        Text(
                                            text = item.category,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (item.category) {
                                                "आरती" -> Color(0xFFC2185B)
                                                "स्तोत्र" -> Color(0xFF512DA8)
                                                "जप" -> Color(0xFF2E7D32)
                                                else -> Color(0xFFE65100)
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.duration,
                                        fontSize = 11.sp,
                                        color = Color(0xFF8D6E63)
                                    )
                                }

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color(0xFFBF360C) else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = item.subtitle,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // View Lyrics Button
                            IconButton(
                                onClick = { viewingLyricsBhajan = item },
                                modifier = Modifier.testTag("view_lyrics_${item.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = "साहित्य / बोल",
                                    tint = Color(0xFF880E4F),
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            // Favorite Icon
                            IconButton(onClick = { onToggleFavorite(item.id) }) {
                                Icon(
                                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (item.isFavorite) Color(0xFFD81B60) else Color(0xFFBCAAA4),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Expanded Bottom Player Card when audio is playing
            if (currentBhajan != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3E2723))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentBhajan.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = currentBhajan.subtitle,
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFE082),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            IconButton(onClick = { viewingLyricsBhajan = currentBhajan }) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "वाचा",
                                    tint = Color(0xFFFFB300)
                                )
                            }
                        }

                        // Progress Slider
                        val progress = if (durationSec > 0) currentSec.toFloat() / durationSec else 0f
                        Slider(
                            value = progress,
                            onValueChange = { newProgress ->
                                onSeekTo((newProgress * durationSec).toInt())
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFFFB300),
                                activeTrackColor = Color(0xFFFFB300),
                                inactiveTrackColor = Color(0x55FFFFFF)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatSeconds(currentSec),
                                fontSize = 11.sp,
                                color = Color(0xFFD7CCC8)
                            )
                            Text(
                                text = currentBhajan.duration,
                                fontSize = 11.sp,
                                color = Color(0xFFD7CCC8)
                            )
                        }

                        // Playback Controls Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Repeat / Loop button
                            IconButton(onClick = onToggleLoop) {
                                Icon(
                                    imageVector = Icons.Default.Repeat,
                                    contentDescription = "Loop",
                                    tint = if (isLooping) Color(0xFFFFB300) else Color(0x77FFFFFF)
                                )
                            }

                            // Previous
                            IconButton(onClick = onPlayPrev) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            // Play/Pause Main
                            FilledIconButton(
                                onClick = onTogglePlayPause,
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = Color(0xFFE65100),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.size(52.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            // Next
                            IconButton(onClick = onPlayNext) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            // Close Player
                            IconButton(onClick = { BhajanPlayerManager.getInstance().stop() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Stop",
                                    tint = Color(0x77FFFFFF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Full Lyrics Modal Bottom Sheet
    if (viewingLyricsBhajan != null) {
        ModalBottomSheet(
            onDismissRequest = { viewingLyricsBhajan = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFFFFFBF7)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = viewingLyricsBhajan?.title ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF360C)
                        )
                        Text(
                            text = viewingLyricsBhajan?.subtitle ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFF5D4037)
                        )
                    }

                    // Font Size Buttons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (lyricsFontSize > 13.sp) lyricsFontSize = (lyricsFontSize.value - 2).sp
                            }
                        ) {
                            Text("अ-", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFE65100))
                        }
                        IconButton(
                            onClick = {
                                if (lyricsFontSize < 26.sp) lyricsFontSize = (lyricsFontSize.value + 2).sp
                            }
                        ) {
                            Text("अ+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE65100))
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFFFE082))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .padding(bottom = 24.dp)
                ) {
                    item {
                        Text(
                            text = viewingLyricsBhajan?.lyrics ?: "",
                            fontSize = lyricsFontSize,
                            color = Color(0xFF2C241E),
                            lineHeight = (lyricsFontSize.value * 1.6f).sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
