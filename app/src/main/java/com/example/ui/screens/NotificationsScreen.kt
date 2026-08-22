package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.ui.components.DevotionalTopAppBar

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            DevotionalTopAppBar(
                title = "🔔 मंदिर सूचना व अपडेट्स",
                subtitle = "श्री संत गजानन महाराज मंदिर घिर्णी",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (notifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "सध्या कोणतीही नवीन सूचना नाही.",
                            fontSize = 14.sp,
                            color = Color(0xFF8D6E63)
                        )
                    }
                }
            }

            items(notifications, key = { it.id }) { notif ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notif_item_${notif.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    when (notif.type) {
                                        "LIVE" -> Color(0xFFFFEBEE)
                                        "DARSHAN" -> Color(0xFFFFF3E0)
                                        "UTSAV" -> Color(0xFFEDE7F6)
                                        else -> Color(0xFFE8F5E9)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (notif.type) {
                                    "LIVE" -> Icons.Default.Videocam
                                    "DARSHAN" -> Icons.Default.Visibility
                                    "UTSAV" -> Icons.Default.Celebration
                                    else -> Icons.Default.Notifications
                                },
                                contentDescription = null,
                                tint = when (notif.type) {
                                    "LIVE" -> Color(0xFFD32F2F)
                                    "DARSHAN" -> Color(0xFFE65100)
                                    "UTSAV" -> Color(0xFF673AB7)
                                    else -> Color(0xFF2E7D32)
                                },
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = notif.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = notif.timestamp,
                                    fontSize = 10.sp,
                                    color = Color(0xFF8D6E63)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notif.message,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
