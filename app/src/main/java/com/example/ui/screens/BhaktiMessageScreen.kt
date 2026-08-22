package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.suvichar.DailySuvicharManager
import com.example.data.model.BhaktiMessage
import com.example.data.model.DailySuvichar
import com.example.ui.components.DailySuvicharPhotoCard
import com.example.ui.components.DevotionalTopAppBar
import com.example.ui.util.DailyCardImageGenerator
import java.util.Calendar

@Composable
fun BhaktiMessageScreen(
    messages: List<BhaktiMessage>,
    todayMarathiDate: String,
    todaySuvichar: String,
    devoteeName: String,
    onDevoteeNameChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var expandedSuvichar by remember { mutableStateOf<DailySuvichar?>(null) }

    val monthSuvichars = remember(selectedMonth) {
        DailySuvicharManager.allSuvichars.filter { it.month == selectedMonth }
    }

    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "📿 आजचा सुविचार व भक्ती संदेश",
                subtitle = "३६५ दिवसांचे दैनिक सुविचार संग्रह",
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
            // 1. Featured Daily Photo Card for Today
            item {
                DailySuvicharPhotoCard(
                    dateMarathi = todayMarathiDate,
                    suvicharText = todaySuvichar,
                    devoteeName = devoteeName,
                    onDevoteeNameChange = onDevoteeNameChange
                )
            }

            // 2. 365-Day Suvichar Explorer Header & Month Tabs
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "📅 ३६५ दिवसांचे सुविचार संग्रह",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBF360C)
                    )
                    Text(
                        text = "महिना निवडून कोणत्याही दिवसाचा सुविचार वाचा व कार्ड शेअर करा:",
                        fontSize = 12.sp,
                        color = Color(0xFF5D4037)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items((1..12).toList()) { monthNum ->
                            val isSelected = selectedMonth == monthNum
                            val monthName = DailySuvicharManager.getMarathiMonthName(monthNum)

                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedMonth = monthNum },
                                label = {
                                    Text(
                                        text = monthName,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFE65100),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFFFFF3E0),
                                    labelColor = Color(0xFF4E342E)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    }
                }
            }

            // 3. Suvichar list for Selected Month
            items(monthSuvichars) { item ->
                val dateLabel = "${DailySuvicharManager.toMarathiDigits(item.day)} ${DailySuvicharManager.getMarathiMonthName(item.month)}"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFE082))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF880E4F)
                            ) {
                                Text(
                                    text = dateLabel,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // 1-Click Generate & Share Card for this day
                                IconButton(
                                    onClick = {
                                        DailyCardImageGenerator.shareCardImage(
                                            context = context,
                                            dateMarathi = dateLabel,
                                            suvicharText = item.suvichar,
                                            devoteeName = devoteeName
                                        )
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "कार्ड शेअर करा",
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "“${item.suvichar}”",
                            fontSize = 14.sp,
                            color = Color(0xFF2C241E),
                            lineHeight = 21.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "॥ गण गण गणात बोते ॥",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )

                            TextButton(
                                onClick = {
                                    DailyCardImageGenerator.shareCardImage(
                                        context = context,
                                        dateMarathi = dateLabel,
                                        suvicharText = item.suvichar,
                                        devoteeName = devoteeName
                                    )
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "📤 फोटो कार्ड शेअर करा",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Additional Classic Amrutvachane
            if (messages.isNotEmpty()) {
                item {
                    Text(
                        text = "अमृतवचने व बोधप्रद विचार",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBF360C),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(messages) { msg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFE082))
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = msg.date,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                                IconButton(
                                    onClick = {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "॥ गण गण गणात बोते ॥ 🙏\n\n\"${msg.quote}\"\n\n- ${msg.author}\nश्री संत गजानन महाराज मंदिर घिर्णी (बुलढाणा)"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "शेअर करा"))
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF8D6E63), modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“${msg.quote}”",
                                fontSize = 13.sp,
                                color = Color(0xFF3E2723),
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "- ${msg.author}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF880E4F)
                            )
                        }
                    }
                }
            }
        }
    }
}
