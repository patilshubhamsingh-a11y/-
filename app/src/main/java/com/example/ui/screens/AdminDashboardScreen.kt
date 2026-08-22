package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.AdminAuthManager
import com.example.data.model.DailyDarshan
import com.example.data.model.LiveDarshanConfig
import com.example.data.repository.TempleRepository
import com.example.ui.components.DevotionalTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    dailyDarshan: DailyDarshan,
    liveConfig: LiveDarshanConfig,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val authManager = remember { AdminAuthManager.getInstance() }
    val repository = remember { TempleRepository.getInstance() }

    val isAdminLoggedIn by authManager.isAdminLoggedIn.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("दैनिक दर्शन", "थेट दर्शन", "सूचना", "गॅलरी व माहिती")

    // Daily Darshan Form State
    var photoUrl by remember(dailyDarshan) { mutableStateOf(dailyDarshan.photoUrl) }
    var marathiDate by remember(dailyDarshan) { mutableStateOf(dailyDarshan.marathiDate) }
    var blessingMsg by remember(dailyDarshan) { mutableStateOf(dailyDarshan.blessingMessage) }
    var isDarshanPublished by remember(dailyDarshan) { mutableStateOf(dailyDarshan.published) }

    // Live Darshan Form State
    var isLiveEnabled by remember(liveConfig) { mutableStateOf(liveConfig.isLive) }
    var streamUrl by remember(liveConfig) { mutableStateOf(liveConfig.streamUrl) }
    var liveTitle by remember(liveConfig) { mutableStateOf(liveConfig.title) }

    // Notification Form State
    var notifTitle by remember { mutableStateOf("") }
    var notifMsg by remember { mutableStateOf("") }

    // Gallery Form State
    var galTitle by remember { mutableStateOf("") }
    var galCategory by remember { mutableStateOf("घिर्णी मंदिर") }
    var galImageUrl by remember { mutableStateOf("") }
    var galDesc by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "⚙️ मंदिर व्यवस्थापन (Admin)",
                subtitle = if (isAdminLoggedIn) "अधिकृत व्यवस्थापक पॅनल" else "प्रवेश पडताळणी",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    if (isAdminLoggedIn) {
                        IconButton(
                            onClick = {
                                authManager.logout()
                                Toast.makeText(context, "लॉगआउट झाले", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.testTag("admin_logout_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (!isAdminLoggedIn) {
            // Admin Login Screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF3E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "व्यवस्थापक लॉगिन (Admin)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF360C)
                        )
                        Text(
                            text = "कृपया अधिकृत व्यवस्थापन पिन प्रविष्ट करा",
                            fontSize = 12.sp,
                            color = Color(0xFF5D4037)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = pinInput,
                            onValueChange = {
                                pinInput = it
                                loginError = false
                            },
                            label = { Text("पासवर्ड / पिन") },
                            placeholder = { Text("उदा. 7777 किंवा Ghirni@2026") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            isError = loginError,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_pin_input")
                        )

                        if (loginError) {
                            Text(
                                text = "चुकीचा पिन! कृपया योग्य पिन टाका.",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (authManager.authenticate(pinInput)) {
                                    loginError = false
                                    Toast.makeText(context, "व्यवस्थापक स्वागत आहे!", Toast.LENGTH_SHORT).show()
                                } else {
                                    loginError = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("admin_login_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE65100),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("प्रवेश करा (Login)", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "सूचना: डीफॉल्ट चाचणी पिन: 7777",
                            fontSize = 11.sp,
                            color = Color(0xFF8D6E63)
                        )
                    }
                }
            }
        } else {
            // Logged-in Admin Management Dashboard
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                // Scrollable or Primary Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color(0xFFE65100)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedTab) {
                        0 -> {
                            // Daily Darshan Settings
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "🙏 दैनिक दर्शन अपडेट करा",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFBF360C)
                                        )

                                        OutlinedTextField(
                                            value = photoUrl,
                                            onValueChange = { photoUrl = it },
                                            label = { Text("नवीन फोटो URL (Firebase Storage / Web URL)") },
                                            placeholder = { Text("रिकामे ठेवल्यास स्थानिक फोटो दिसेल") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        OutlinedTextField(
                                            value = marathiDate,
                                            onValueChange = { marathiDate = it },
                                            label = { Text("तारीख व दिवस") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        OutlinedTextField(
                                            value = blessingMsg,
                                            onValueChange = { blessingMsg = it },
                                            label = { Text("दैनिक भक्ती आशीर्वाद संदेश") },
                                            maxLines = 3,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "अॅपमध्ये दर्शन प्रकाशित करा (Publish):",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Switch(
                                                checked = isDarshanPublished,
                                                onCheckedChange = { isDarshanPublished = it },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color.White,
                                                    checkedTrackColor = Color(0xFF2E7D32)
                                                )
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                repository.updateDailyDarshan(
                                                    photoUrl = photoUrl,
                                                    message = blessingMsg,
                                                    marathiDate = marathiDate,
                                                    published = isDarshanPublished
                                                )
                                                Toast.makeText(context, "दैनिक दर्शन यशस्वीरित्या अपडेट झाले!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("सेव्ह करा व प्रकाशित करा")
                                        }
                                    }
                                }
                            }
                        }

                        1 -> {
                            // Live Darshan Settings
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "🔴 थेट दर्शन प्रणाली नियंत्रण (Live Stream)",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD32F2F)
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "थेट दर्शन स्थिती (Live ON/OFF):",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = if (isLiveEnabled) "थेट दर्शन सुरू आहे" else "थेट दर्शन बंद (ऑफलाइन)",
                                                    fontSize = 12.sp,
                                                    color = if (isLiveEnabled) Color(0xFF2E7D32) else Color(0xFF8D6E63)
                                                )
                                            }
                                            Switch(
                                                checked = isLiveEnabled,
                                                onCheckedChange = { isLiveEnabled = it },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color.White,
                                                    checkedTrackColor = Color(0xFFD32F2F)
                                                )
                                            )
                                        }

                                        OutlinedTextField(
                                            value = streamUrl,
                                            onValueChange = { streamUrl = it },
                                            label = { Text("सुरक्षित HLS / RTSP प्रवाह URL") },
                                            placeholder = { Text("उदा. https://stream.temple.com/live/ghirni.m3u8") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        OutlinedTextField(
                                            value = liveTitle,
                                            onValueChange = { liveTitle = it },
                                            label = { Text("थेट दर्शनाचे शीर्षक") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        Button(
                                            onClick = {
                                                repository.updateLiveDarshanConfig(
                                                    isLive = isLiveEnabled,
                                                    streamUrl = streamUrl,
                                                    title = liveTitle
                                                )
                                                Toast.makeText(context, "थेट दर्शन सेटिंग्ज अपडेट झाल्या!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("थेट दर्शन सेटिंग्ज जतन करा")
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            // Send Notification
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "🔔 भक्तांना सूचना पाठवा (Broadcast)",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFBF360C)
                                        )

                                        OutlinedTextField(
                                            value = notifTitle,
                                            onValueChange = { notifTitle = it },
                                            label = { Text("सूचनेचे शीर्षक") },
                                            placeholder = { Text("उदा. गुरुवार विशेष महाप्रसाद") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        OutlinedTextField(
                                            value = notifMsg,
                                            onValueChange = { notifMsg = it },
                                            label = { Text("सूचनेचा संदेश") },
                                            placeholder = { Text("भाविकांसाठी महत्त्वाची माहिती...") },
                                            maxLines = 3,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        Button(
                                            onClick = {
                                                if (notifTitle.isNotBlank() && notifMsg.isNotBlank()) {
                                                    repository.sendNotification(notifTitle, notifMsg, "GENERAL")
                                                    notifTitle = ""
                                                    notifMsg = ""
                                                    Toast.makeText(context, "सूचना पाठवली गेली!", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "कृपया शीर्षक व संदेश भरा", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("सूचना पाठवा")
                                        }
                                    }
                                }
                            }
                        }

                        3 -> {
                            // Gallery Add & Local Photos Replacement Info
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "🖼️ गॅलरीमध्ये फोटो जोडा",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF00897B)
                                        )

                                        OutlinedTextField(
                                            value = galTitle,
                                            onValueChange = { galTitle = it },
                                            label = { Text("फोटोचे शीर्षक") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        OutlinedTextField(
                                            value = galImageUrl,
                                            onValueChange = { galImageUrl = it },
                                            label = { Text("फोटो URL (Image URL)") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        OutlinedTextField(
                                            value = galDesc,
                                            onValueChange = { galDesc = it },
                                            label = { Text("वर्णन (Description)") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        Button(
                                            onClick = {
                                                if (galTitle.isNotBlank() && galImageUrl.isNotBlank()) {
                                                    repository.addGalleryItem(galTitle, galCategory, galImageUrl, galDesc)
                                                    galTitle = ""
                                                    galImageUrl = ""
                                                    galDesc = ""
                                                    Toast.makeText(context, "फोटो गॅलरीत जोडला गेला!", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "कृपया शीर्षक व URL टाका", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("गॅलरीत जोडा")
                                        }
                                    }
                                }
                            }

                            // Clear Marathi Instruction for Local Assets Replacement
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "📁 स्थानिक फोटो बदलण्यासाठी मार्गदर्शक (Local Photos):",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFBF360C)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "घिर्णी मंदिराचे आणि महाराजांचे प्रत्यक्ष मूळ फोटो लावण्यासाठी Android Studio मध्ये खालील फोल्डरमध्ये फोटो कॉपी करावेत:\n\n📂 फोल्डर:\napp/src/main/res/drawable/\n\n🏷️ फोटोची नावे:\n१. gajanan_maharaj.jpg (महाराजांचा मुख्य फोटो)\n२. ghirni_temple.jpg (घिर्णी मंदिराचा फोटो)\n३. daily_darshan_today.jpg (आजच्या दर्शनाचा फोटो)",
                                            fontSize = 12.sp,
                                            color = Color(0xFF3E2723),
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
