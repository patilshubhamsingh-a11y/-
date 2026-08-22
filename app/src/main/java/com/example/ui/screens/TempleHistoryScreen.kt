package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TempleHistorySection
import com.example.ui.components.DevotionalTopAppBar

@Composable
fun TempleHistoryScreen(
    historySections: List<TempleHistorySection>,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "🛕 मंदिराचा इतिहास",
                subtitle = "घिर्णी येथील श्री गजानन महाराज मंदिर ('प्रति शेगाव')",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Banner of Ghirni Temple
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ghirni_temple),
                            contentDescription = "घिर्णी मंदिर",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xCC000000))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE65100)
                            ) {
                                Text(
                                    text = "पंचक्रोशीतील 'प्रति शेगाव'",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "श्री संत गजानन महाराज मंदिर, घिर्णी",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ता. मलकापूर, जि. बुलढाणा, महाराष्ट्र",
                                color = Color(0xFFFFE082),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Introduction Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFB300))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🕉️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "घिर्णी येथील श्री संत गजानन महाराज मंदिराची पावन निर्मिती, गावकऱ्यांची भक्ती, अष्टकोनी रचना आणि महाराजांच्या अगाध कृपेचा संपूर्ण इतिहास येथे सादर केला आहे.",
                            fontSize = 13.sp,
                            color = Color(0xFF5D4037),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // History Sections Accordion / Cards
            items(historySections, key = { it.id }) { section ->
                var isExpanded by remember { mutableStateOf(true) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { isExpanded = !isExpanded }
                        .testTag("history_card_${section.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (section.id) {
                                            "history_10" -> Color(0xFFE0F7FA) // Water Borewell miracle
                                            "history_9" -> Color(0xFFFFF8E1) // Prati Shegaon
                                            "history_5" -> Color(0xFFFCE4EC) // Structure
                                            else -> Color(0xFFFFF3E0)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (section.iconName) {
                                        "water" -> Icons.Default.WaterDrop
                                        "star" -> Icons.Default.Star
                                        "temple_structure" -> Icons.Default.Apartment
                                        "architecture" -> Icons.Default.Foundation
                                        "location" -> Icons.Default.PinDrop
                                        "management" -> Icons.Default.Groups
                                        "prasad" -> Icons.Default.Restaurant
                                        "event" -> Icons.Default.Celebration
                                        else -> Icons.Default.AutoAwesome
                                    },
                                    contentDescription = null,
                                    tint = when (section.id) {
                                        "history_10" -> Color(0xFF00838F)
                                        "history_9" -> Color(0xFFE65100)
                                        else -> Color(0xFF880E4F)
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = section.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFBF360C)
                                )
                                Text(
                                    text = section.subtitle,
                                    fontSize = 11.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color(0xFFBCAAA4)
                            )
                        }

                        if (section.highlight.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (section.id == "history_10") Color(0xFFE0F2F1) else Color(0xFFFFF8E1)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (section.id == "history_10") "💧" else "🚩",
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = section.highlight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (section.id == "history_10") Color(0xFF00695C) else Color(0xFFE65100)
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = section.content,
                                    fontSize = 13.sp,
                                    color = Color(0xFF3E2723),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }

            // Trust Management & Leadership Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEBE9))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "श्री संत गजानन महाराज मंदिर ट्रस्ट घिर्णी",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4E342E)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• अध्यक्ष: श्री चिंधु रतन पाटील\n• दर्जा: शासकीय 'क' वर्ग तीर्थक्षेत्र\n• पत्ता: घिर्णी, ता. मलकापूर, जि. बुलढाणा (महाराष्ट्र)\n• गुरुवार अन्नदान व नित्य सेवा अखंड सुरू",
                            fontSize = 12.sp,
                            color = Color(0xFF4E342E),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
