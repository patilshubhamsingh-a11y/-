package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LiveDarshanConfig
import com.example.player.LiveStreamPlayer
import com.example.ui.components.DevotionalTopAppBar

@Composable
fun LiveDarshanScreen(
    liveConfig: LiveDarshanConfig,
    onNavigateBack: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    var isFullscreen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!isFullscreen) {
                DevotionalTopAppBar(
                    title = "🔴 थेट दर्शन",
                    subtitle = "घिर्णी मंदिर (ता. मलकापूर)",
                    canNavigateBack = true,
                    onNavigateBack = onNavigateBack,
                    actions = {
                        IconButton(
                            onClick = onNavigateToAdmin,
                            modifier = Modifier.testTag("live_admin_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Config",
                                tint = Color(0xFFE65100)
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(if (isFullscreen) PaddingValues(0.dp) else innerPadding),
            contentPadding = if (isFullscreen) PaddingValues(0.dp) else PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Video Player Box or Offline State
            item {
                if (liveConfig.isLive && liveConfig.streamUrl.isNotBlank()) {
                    // Active Live Player
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isFullscreen) 500.dp else 260.dp)
                            .shadow(6.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(if (isFullscreen) 0.dp else 16.dp)
                    ) {
                        LiveStreamPlayer(
                            streamUrl = liveConfig.streamUrl,
                            title = liveConfig.title,
                            isFullscreen = isFullscreen,
                            onFullscreenToggle = { isFullscreen = !isFullscreen }
                        )
                    }
                } else {
                    // Offline / Upcoming Stream Placeholder
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
                                        colors = listOf(Color(0xFFFFF3E0), MaterialTheme.colorScheme.surface)
                                    )
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Temple & Camera Indicator Graphic
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.5.dp, Color(0xFFFFB300), RoundedCornerShape(14.dp))
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
                                        .background(Color(0x99000000)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xDD880E4F)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VideocamOff,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        Text(
                                            text = "सध्या थेट दर्शन उपलब्ध नाही",
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFE65100)
                                        ) {
                                            Text(
                                                text = "थेट दर्शन लवकरच सुरू होईल",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "॥ गण गण गणात बोते ॥",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = liveConfig.offlineMessage,
                                fontSize = 13.sp,
                                color = Color(0xFF5D4037),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // Live Stream Architecture & Security Information Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF7B1FA2)
                            )
                            Text(
                                text = "सुरक्षित थेट दर्शन प्रणाली (Future-Ready)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• मंदिरातील Hikvision NVR DS-7632NXI-K2 मुख्य कॅमेऱ्यातून येणारा अधिकृत HLS/RTSP प्रवाह सुरक्षित बॅकएंडवरून व्यवस्थापित केला जातो.\n• कोणतीही संवेदनशील माहिती किंवा पासवर्ड अॅपमध्ये उघडा न करता सुरक्षितपणे थेट दर्शन जोडता येते.\n• व्यवस्थापकांद्वारे 'थेट दर्शन सुरू' केल्यावर भाविकांच्या अॅपमध्ये आपोआप व्हिडिओ दिसेल.",
                            fontSize = 12.sp,
                            color = Color(0xFF4A148C),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Live Aarti Schedule Timing Card
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color(0xFFE65100)
                            )
                            Text(
                                text = "थेट दर्शन प्रक्षेपण वेळा",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TimingRow(time = "०६:०० AM - ०६:३० AM", title = "🌅 प्रातः काकड आरती")
                        TimingRow(time = "१२:०० PM - १२:३० PM", title = "☀️ मध्यान्ह महाआरती")
                        TimingRow(time = "०७:०० PM - ०७:३० PM", title = "🪔 संध्या आरती व धुपारती")
                        TimingRow(time = "०९:०० PM - ०९:२० PM", title = "🌙 शेजारती")
                        TimingRow(time = "विशेष उत्सव", title = "🚩 प्रगटदिन, एकादशी, गुरुवार")
                    }
                }
            }
        }
    }
}
