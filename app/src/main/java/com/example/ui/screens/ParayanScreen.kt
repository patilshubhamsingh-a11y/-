package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChapterItem
import com.example.ui.components.DevotionalTopAppBar

@Composable
fun ParayanScreen(
    chapters: List<ChapterItem>,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var readingChapter by remember { mutableStateOf<ChapterItem?>(null) }
    var fontSize by remember { mutableStateOf(16.sp) }

    val filteredChapters = remember(searchQuery, chapters) {
        if (searchQuery.isBlank()) chapters
        else chapters.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.summary.contains(searchQuery, ignoreCase = true) ||
                    it.fullText.contains(searchQuery, ignoreCase = true)
        }
    }

    if (readingChapter != null) {
        // Detailed Chapter Reader
        val chapter = readingChapter!!
        Scaffold(
            topBar = {
                DevotionalTopAppBar(
                    title = chapter.title,
                    subtitle = "श्री गजानन विजय ग्रंथ पारायण",
                    canNavigateBack = true,
                    onNavigateBack = { readingChapter = null },
                    actions = {
                        IconButton(
                            onClick = {
                                if (fontSize > 13.sp) fontSize = (fontSize.value - 2).sp
                            }
                        ) {
                            Text("अ-", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFE65100))
                        }
                        IconButton(
                            onClick = {
                                if (fontSize < 26.sp) fontSize = (fontSize.value + 2).sp
                            }
                        ) {
                            Text("अ+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE65100))
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFBF7))
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "📖 अध्यायाचा सारांश",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = chapter.summary,
                                fontSize = 13.sp,
                                color = Color(0xFF5D4037),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Full Pothi / Marathi Verses Content
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFE082))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = chapter.fullText,
                                fontSize = fontSize,
                                color = Color(0xFF2C241E),
                                lineHeight = (fontSize.value * 1.7f).sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Footer Mantra
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "॥ श्री गजानन विजय ग्रंथ पारायण ॥\n॥ गण गण गणात बोते ॥",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF360C),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    } else {
        // Chapters List Screen
        Scaffold(
            topBar = {
                DevotionalTopAppBar(
                    title = "📖 श्री गजानन विजय",
                    subtitle = "संत दासगणू विरचित संपूर्ण पारायण",
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
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("श्री. गजानन विजय पारायण किंवा अध्याय शोधा...", fontSize = 13.sp) },
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
                        .padding(16.dp)
                        .testTag("parayan_search_field")
                )

                // List of Chapters
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredChapters, key = { it.chapterNumber }) { chap ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { readingChapter = chap }
                                .testTag("chapter_card_${chap.chapterNumber}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFF3E0))
                                        .border(1.dp, Color(0xFFFFB300), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${chap.chapterNumber}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFBF360C)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = chap.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = chap.summary,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color(0xFFBCAAA4)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
