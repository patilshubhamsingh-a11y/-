package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DevotionalTopAppBar

@Composable
fun TempleLocationScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "📍 मंदिराचा मार्ग",
                subtitle = "घिर्णी, ता. मलकापूर, जि. बुलढाणा",
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
            // Main Address Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFFFF3E0), MaterialTheme.colorScheme.surface)
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE65100)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "श्री संत गजानन महाराज मंदिर",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFBF360C)
                                )
                                Text(
                                    text = "घिर्णी ('प्रति शेगाव')",
                                    fontSize = 13.sp,
                                    color = Color(0xFFE65100),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "पत्ता:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "श्री संत गजानन महाराज मंदिर, घिर्णी\nतालुका: मलकापूर, जिल्हा: बुलढाणा\nमहाराष्ट्र - ४४३१०१",
                            fontSize = 14.sp,
                            color = Color(0xFF2C241E),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Google Maps Action Button
                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("geo:0,0?q=Gajanan+Maharaj+Temple+Ghirni+Malkapur+Buldhana")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                try {
                                    context.startActivity(mapIntent)
                                } catch (e: Exception) {
                                    val webIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Gajanan+Maharaj+Temple+Ghirni+Malkapur+Buldhana")
                                    )
                                    context.startActivity(webIntent)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("open_maps_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Google Maps मध्ये मार्ग पहा (दिशा)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Travel Routes Guide
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "कसे पोहोचावे? (मार्गदर्शक)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF360C)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Railway Route
                        TravelOptionItem(
                            icon = Icons.Default.Train,
                            iconColor = Color(0xFF1565C0),
                            title = "रेल्वेने (Railway)",
                            desc = "जवळचे मुख्य रेल्वे स्थानक: मलकापूर (MKU - मध्य रेल्वे मुख्य लाईन). मलकापूर रेल्वे स्थानकावरून घिर्णी गावासाठी रिक्षा, खाजगी वाहने व एसटी बस उपलब्ध असतात."
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE0D5C7))

                        // Bus / Road Route
                        TravelOptionItem(
                            icon = Icons.Default.DirectionsBus,
                            iconColor = Color(0xFFE65100),
                            title = "बसने / रस्ता मार्गाने (Road / Bus)",
                            desc = "मलकापूर बस स्थानकावरून घिर्णी येथे जाण्यासाठी नियमित बसेस व स्थानिक वाहने उपलब्ध आहेत. राष्ट्रीय महामार्ग क्र. ६ (NH-6) वरून सहज पोहोचता येते."
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE0D5C7))

                        // Shegaon to Ghirni Distance
                        TravelOptionItem(
                            icon = Icons.Default.Route,
                            iconColor = Color(0xFF880E4F),
                            title = "शेगाव ते घिर्णी अंतर",
                            desc = "शेगाव (गजानन महाराज समाधी मंदिर) येथून घिर्णी मंदिर रस्ता मार्गाने जवळ असून एकाच दिवसात दोन्ही मंदिरांचे दर्शन सुलभतेने घेता येते."
                        )
                    }
                }
            }

            // Darshan Advice Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🚩", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "दर गुरुवारी मंदिरात पायदळ दिंड्या व महाप्रसादाचे आयोजन असते. भाविकांनी आवर्जून गुरुवारच्या महाप्रसादाचा लाभ घ्यावा.",
                            fontSize = 12.sp,
                            color = Color(0xFF5D4037),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TravelOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}
