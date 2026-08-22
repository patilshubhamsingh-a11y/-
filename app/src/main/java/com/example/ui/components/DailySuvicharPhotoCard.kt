package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.example.R
import com.example.ui.util.DailyCardImageGenerator

@Composable
fun DailySuvicharPhotoCard(
    dateMarathi: String,
    suvicharText: String,
    devoteeName: String = "प्रिय भक्त",
    onDevoteeNameChange: (String) -> Unit = {},
    customPhotoDrawableRes: Int = R.drawable.gajanan_maharaj,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showNameDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(devoteeName) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp))
            .testTag("daily_suvichar_photo_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFFB300), Color(0xFFE65100))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF3E0), // Soft cream saffron
                            Color(0xFFFFFDE7), // Light golden tint
                            Color(0xFFFFFFFF),
                            Color(0xFFFFF8E1)
                        )
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Temple Header Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFBF360C),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFBF360C), Color(0xFFE65100))
                            )
                        )
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "श्री संत गजानन महाराज मंदिर घिर्णी",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "घिर्णी, ता. मलकापूर, जि. बुलढाणा",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFFE082),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Photo of Shri Gajanan Maharaj with Golden Aura Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(3.dp, Color(0xFFFFB300), RoundedCornerShape(18.dp))
                    .shadow(4.dp, RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = customPhotoDrawableRes),
                    contentDescription = "श्री संत गजानन महाराज",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Temple Badge on Top Left
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    color = Color(0xDD880E4F),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "🛕 घिर्णी मंदिर (प्रति शेगाव)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Marathi Date Badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF880E4F),
                modifier = Modifier.testTag("daily_card_date_badge")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = dateMarathi,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Daily Suvichar Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFFFFCC80), Color(0xFFFFB300))
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🌸 आजचा सुविचार 🌸",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF360C)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "“ $suvicharText ”",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C241E),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Devotee Name (भक्त : [Name])
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFF3E0),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFFFCC80), RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "भक्त : ${if (devoteeName.isBlank()) "प्रिय भक्त" else devoteeName}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4E342E)
                        )
                    }

                    TextButton(
                        onClick = {
                            tempName = if (devoteeName == "प्रिय भक्त") "" else devoteeName
                            showNameDialog = true
                        },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "नाव बदला", modifier = Modifier.size(14.dp), tint = Color(0xFFE65100))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("नाव बदला", fontSize = 11.sp, color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Gan Gan Ganat Bote (ठळक अक्षरात)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE65100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "॥ गण गण गणात बोते ॥ 🙏",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7. Action Buttons: 📤 शेअर करा & 📋 कॉपी करा
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Main Share Button (Generates Image & Opens Android Share Sheet)
                Button(
                    onClick = {
                        DailyCardImageGenerator.shareCardImage(
                            context = context,
                            dateMarathi = dateMarathi,
                            suvicharText = suvicharText,
                            devoteeName = devoteeName
                        )
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("share_daily_card_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("📤 कार्ड शेअर करा", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Copy Text Button
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(
                            "आजचा सुविचार",
                            "॥ गण गण गणात बोते ॥ 🙏\n\n*श्री संत गजानन महाराज मंदिर घिर्णी*\n📅 तारीख: $dateMarathi\n\n🌸 *आजचा सुविचार:*\n\"$suvicharText\"\n\n🚩 भक्त: $devoteeName\n॥ गण गण गणात बोते ॥"
                        )
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "सुविचार कॉपी केला!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("copy_suvichar_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFBF360C)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE65100))
                    )
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("कॉपी", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Devotee Name Edit Dialog
    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = {
                Text(
                    text = "आपले नाव प्रविष्ट करा",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFBF360C)
                )
            },
            text = {
                Column {
                    Text(
                        text = "फोटो कार्डवर 'भक्त : [नाव]' असे दिसेल:",
                        fontSize = 13.sp,
                        color = Color(0xFF5D4037)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("भक्ताचे नाव") },
                        placeholder = { Text("उदा. शुभम पाटील") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val finalName = if (tempName.isBlank()) "प्रिय भक्त" else tempName.trim()
                        onDevoteeNameChange(finalName)
                        showNameDialog = false
                        Toast.makeText(context, "नाव सेव्ह झाले: $finalName", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                ) {
                    Text("जतन करा", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text("रद्द करा", color = Color(0xFF8D6E63))
                }
            }
        )
    }
}
