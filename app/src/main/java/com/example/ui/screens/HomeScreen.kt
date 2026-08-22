package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ads.AdMobBanner
import com.example.data.model.DailyDarshan
import com.example.data.model.LiveDarshanConfig
import com.example.ui.components.DailySuvicharPhotoCard
import com.example.ui.components.DevotionalTopAppBar
import com.example.ui.components.MantraChantCard
import com.example.ui.components.MenuFeatureCard

@Composable
fun HomeScreen(
    dailyDarshan: DailyDarshan,
    liveConfig: LiveDarshanConfig,
    japCount: Int,
    unreadNotifCount: Int,
    todayMarathiDate: String,
    todaySuvichar: String,
    devoteeName: String,
    onDevoteeNameChange: (String) -> Unit,
    onJapClick: () -> Unit,
    onResetJap: () -> Unit,
    onNavigateToDarshan: () -> Unit,
    onNavigateToLive: () -> Unit,
    onNavigateToBhajans: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToParayan: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToLocation: () -> Unit,
    onNavigateToBhaktiMsg: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "श्री संत गजानन महाराज मंदिर घिर्णी",
                subtitle = "ता. मलकापूर, जि. बुलढाणा, महाराष्ट्र",
                actions = {
                    IconButton(
                        onClick = onNavigateToNotifications,
                        modifier = Modifier.testTag("home_notif_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifCount > 0) {
                                    Badge { Text("$unreadNotifCount") }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "सूचना",
                                tint = Color(0xFFE65100)
                            )
                        }
                    }
                    IconButton(
                        onClick = onNavigateToAdmin,
                        modifier = Modifier.testTag("home_admin_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = Color(0xFF880E4F)
                        )
                    }
                }
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
            // 1. Daily Suvichar + Date-Wise Photo Card (New Feature)
            item {
                DailySuvicharPhotoCard(
                    dateMarathi = todayMarathiDate,
                    suvicharText = todaySuvichar,
                    devoteeName = devoteeName,
                    onDevoteeNameChange = onDevoteeNameChange
                )
            }

            // 2. Interactive Mantra Chant & Continuous Jap Counter
            item {
                MantraChantCard(
                    count = japCount,
                    onChantClick = onJapClick,
                    onResetClick = onResetJap
                )
            }

            // Quick Live Darshan Status Alert Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = onNavigateToLive),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (liveConfig.isLive) Color(0xFFFFEBEE) else Color(0xFFFFF8E1)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            if (liveConfig.isLive)
                                listOf(Color(0xFFD32F2F), Color(0xFFFF5252))
                            else
                                listOf(Color(0xFFFFB300), Color(0xFFFFCC80))
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (liveConfig.isLive) Color(0xFFD32F2F) else Color(0xFFFF8F00))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (liveConfig.isLive) "🔴 थेट दर्शन सुरू आहे" else "🔴 थेट दर्शन",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (liveConfig.isLive) Color(0xFFB71C1C) else Color(0xFFBF360C)
                            )
                            Text(
                                text = if (liveConfig.isLive) "थेट प्रवाह पाहण्यासाठी येथे दाबा" else "सध्या ऑफलाइन | लवकरच सुरू होईल",
                                fontSize = 11.sp,
                                color = Color(0xFF5D4037)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = null,
                            tint = if (liveConfig.isLive) Color(0xFFD32F2F) else Color(0xFFFF8F00),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Menu Section Header
            item {
                Text(
                    text = "भक्ती सेवा व सुविधा",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBF360C),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }

            // Menu Feature Cards
            item {
                MenuFeatureCard(
                    title = "🙏 आजचे दर्शन",
                    subtitle = "दैनिक पावन मूर्ती दर्शन व आशीर्वाद",
                    icon = Icons.Default.Visibility,
                    iconBgColor = Color(0xFFE65100),
                    onClick = onNavigateToDarshan,
                    testTag = "menu_daily_darshan",
                    badgeText = "दैनिक"
                )
            }

            item {
                MenuFeatureCard(
                    title = "🔴 थेट दर्शन (Live Darshan)",
                    subtitle = "मंदिरातील सीसीटीव्ही थेट प्रक्षेपण",
                    icon = Icons.Default.Videocam,
                    iconBgColor = Color(0xFFD32F2F),
                    onClick = onNavigateToLive,
                    testTag = "menu_live_darshan"
                )
            }

            item {
                MenuFeatureCard(
                    title = "🎵 भजने व आरती",
                    subtitle = "आरती, स्तोत्रे, बावन्नी व अखंड नामस्मरण",
                    icon = Icons.Default.MusicNote,
                    iconBgColor = Color(0xFF880E4F),
                    onClick = onNavigateToBhajans,
                    testTag = "menu_bhajans"
                )
            }

            item {
                MenuFeatureCard(
                    title = "🖼️ फोटो संग्रह",
                    subtitle = "महाराज, मंदिर, गुरुवार महाप्रसाद व उत्सव",
                    icon = Icons.Default.PhotoLibrary,
                    iconBgColor = Color(0xFF00897B),
                    onClick = onNavigateToGallery,
                    testTag = "menu_gallery"
                )
            }

            item {
                MenuFeatureCard(
                    title = "📖 महाराजांचे चरित्र (पारायण)",
                    subtitle = "श्री गजानन विजय ग्रंथ - २१ अध्याय",
                    icon = Icons.Default.MenuBook,
                    iconBgColor = Color(0xFF5D4037),
                    onClick = onNavigateToParayan,
                    testTag = "menu_parayan"
                )
            }

            item {
                MenuFeatureCard(
                    title = "🛕 मंदिराचा इतिहास",
                    subtitle = "संकल्पना, अष्टकोनी बांधकाम व बोअरवेलचा अनुभव",
                    icon = Icons.Default.AccountBalance,
                    iconBgColor = Color(0xFFFF8F00),
                    onClick = onNavigateToHistory,
                    testTag = "menu_history",
                    badgeText = "प्रति शेगाव"
                )
            }

            item {
                MenuFeatureCard(
                    title = "📍 मंदिराचा मार्ग",
                    subtitle = "घिर्णी, ता. मलकापूर | Google Maps दिशा",
                    icon = Icons.Default.LocationOn,
                    iconBgColor = Color(0xFF1976D2),
                    onClick = onNavigateToLocation,
                    testTag = "menu_location"
                )
            }

            item {
                MenuFeatureCard(
                    title = "📿 आजचा भक्ती संदेश",
                    subtitle = "दैनिक विचार व महाराजांचे वचन",
                    icon = Icons.Default.FormatQuote,
                    iconBgColor = Color(0xFF7B1FA2),
                    onClick = onNavigateToBhaktiMsg,
                    testTag = "menu_bhakti_msg"
                )
            }

            // Quick Map Button Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "मंदिराला भेट द्या",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "घिर्णी, ता. मलकापूर, जि. बुलढाणा",
                                fontSize = 11.sp,
                                color = Color(0xFF388E3C)
                            )
                        }
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("मार्ग पहा", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Non-intrusive AdMob banner slot
            item {
                AdMobBanner()
            }

            // Bottom Footer
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "॥ अनंतकोटी ब्रह्मांडनायक राजाधिराज योगिराज परमहंस सद्गुरु श्री संत गजानन महाराज की जय ॥",
                        fontSize = 11.sp,
                        color = Color(0xFF8D6E63),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "श्री संत गजानन महाराज मंदिर ट्रस्ट, घिर्णी",
                        fontSize = 10.sp,
                        color = Color(0xFFBCAAA4),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
