package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.DailyDarshan
import com.example.ui.components.DevotionalTopAppBar

@Composable
fun DailyDarshanScreen(
    dailyDarshan: DailyDarshan,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var isZoomed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "🙏 आजचे दर्शन",
                subtitle = dailyDarshan.marathiDate.ifBlank { "श्री संत गजानन महाराज मंदिर, घिर्णी" },
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "॥ गण गण गणात बोते ॥ 🙏\n\nआजचे पावन दर्शन - श्री संत गजानन महाराज मंदिर घिर्णी (बुलढाणा)\n\n\"${dailyDarshan.blessingMessage}\"\n\nप्रति शेगाव घिर्णी मंदिर भक्ती अॅप"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "दर्शन शेअर करा"))
                        },
                        modifier = Modifier.testTag("share_darshan_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color(0xFFE65100)
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
            // Main Daily Photo Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color(0xFFFFB300), RoundedCornerShape(16.dp))
                                .clickable { isZoomed = true }
                                .testTag("daily_darshan_image_box")
                        ) {
                            if (dailyDarshan.photoUrl.isNotBlank()) {
                                AsyncImage(
                                    model = dailyDarshan.photoUrl,
                                    contentDescription = "आजचे दर्शन",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.daily_darshan_today),
                                    contentDescription = "आजचे दर्शन",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // Tap to Zoom hint
                            Surface(
                                shape = RoundedCornerShape(topStart = 10.dp),
                                color = Color(0xAA000000),
                                modifier = Modifier.align(Alignment.BottomEnd)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ZoomIn,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "मोठे करा",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Date Badge
                            Surface(
                                shape = RoundedCornerShape(bottomEnd = 12.dp),
                                color = Color(0xDD880E4F),
                                modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = "आजचे दर्शन • घिर्णी",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "॥ गण गण गणात बोते ॥",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFBF360C),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Blessing Message Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(Color(0xFFFFE082), Color(0xFFFFCC80)))
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = "📿", fontSize = 16.sp)
                                    Text(
                                        text = "आजचा पावन आशीर्वाद संदेश",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE65100)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dailyDarshan.blessingMessage,
                                    fontSize = 14.sp,
                                    color = Color(0xFF3E2723),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }

            // Aarti & Darshan Timings Card
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
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFFE65100)
                            )
                            Text(
                                text = "दैनिक आरती व दर्शन वेळापत्रक",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TimingRow(time = "०६:०० AM", title = "काकड आरती व भूपाळी")
                        TimingRow(time = "१२:०० PM", title = "मध्यान्ह महाआरती व नैवेद्य")
                        TimingRow(time = "०७:०० PM", title = "संध्या आरती व धुपारती")
                        TimingRow(time = "०९:०० PM", title = "शेजारती व मंदिर शयन")
                    }
                }
            }

            // Share Card Action
            item {
                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "॥ गण गण गणात बोते ॥ 🙏\n\nआजचे पावन दर्शन - श्री संत गजानन महाराज मंदिर घिर्णी (बुलढाणा)\n\n\"${dailyDarshan.blessingMessage}\"\n\nप्रति शेगाव घिर्णी मंदिर भक्ती अॅप"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "दर्शन शेअर करा"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE65100),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "हे दर्शन व्हॉट्सअ‍ॅपवर शेअर करा",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Full-screen Image Dialog with Dismiss
    if (isZoomed) {
        Dialog(onDismissRequest = { isZoomed = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { isZoomed = false }
            ) {
                if (dailyDarshan.photoUrl.isNotBlank()) {
                    AsyncImage(
                        model = dailyDarshan.photoUrl,
                        contentDescription = "Full Daily Darshan",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.daily_darshan_today),
                        contentDescription = "Full Daily Darshan",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )
                }

                IconButton(
                    onClick = { isZoomed = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "बंद करा",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimingRow(time: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFFFF3E0)
        ) {
            Text(
                text = time,
                fontSize = 12.sp,
                color = Color(0xFFE65100),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}
