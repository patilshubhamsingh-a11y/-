package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.local.suvichar.DailySuvicharManager
import com.example.data.repository.TempleRepository
import com.example.player.BhajanPlayerManager
import com.example.ui.components.AudioPlayerMiniBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                DevotionalApp()
            }
        }
    }
}

@Composable
fun DevotionalApp() {
    val navController = rememberNavController()
    val repository = remember { TempleRepository.getInstance() }
    val playerManager = remember { BhajanPlayerManager.getInstance() }

    // State Collection
    val dailyDarshan by repository.dailyDarshan.collectAsStateWithLifecycle()
    val liveConfig by repository.liveDarshanConfig.collectAsStateWithLifecycle()
    val bhajans by repository.bhajans.collectAsStateWithLifecycle()
    val galleryItems by repository.gallery.collectAsStateWithLifecycle()
    val chapters by repository.parayanChapters.collectAsStateWithLifecycle()
    val historySections by repository.historySections.collectAsStateWithLifecycle()
    val bhaktiMessages by repository.bhaktiMessages.collectAsStateWithLifecycle()
    val notifications by repository.notifications.collectAsStateWithLifecycle()
    val japCount by repository.japCounter.collectAsStateWithLifecycle()
    val devoteeName by repository.devoteeName.collectAsStateWithLifecycle()
    val customSuvichar by repository.customDailySuvichar.collectAsStateWithLifecycle()

    val todayMarathiDate = remember { DailySuvicharManager.getFormattedMarathiDate() }
    val todaySuvichar = customSuvichar ?: remember { DailySuvicharManager.getTodaySuvichar().suvichar }

    // Player State Collection
    val currentBhajan by playerManager.currentBhajan.collectAsStateWithLifecycle()
    val isPlaying by playerManager.isPlaying.collectAsStateWithLifecycle()
    val currentSec by playerManager.currentPositionSec.collectAsStateWithLifecycle()
    val durationSec by playerManager.durationSec.collectAsStateWithLifecycle()
    val isLooping by playerManager.isLooping.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Show floating Mini Player bar on all screens except when inside Bhajan Screen
            if (currentBhajan != null && currentRoute != "bhajans") {
                AudioPlayerMiniBar(
                    bhajan = currentBhajan!!,
                    isPlaying = isPlaying,
                    currentSec = currentSec,
                    durationSec = durationSec,
                    onPlayPause = { playerManager.togglePlayPause() },
                    onClose = { playerManager.stop() },
                    onBarClick = { navController.navigate("bhajans") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Home Dashboard
            composable("home") {
                HomeScreen(
                    dailyDarshan = dailyDarshan,
                    liveConfig = liveConfig,
                    japCount = japCount,
                    unreadNotifCount = notifications.size,
                    todayMarathiDate = todayMarathiDate,
                    todaySuvichar = todaySuvichar,
                    devoteeName = devoteeName,
                    onDevoteeNameChange = { repository.updateDevoteeName(it) },
                    onJapClick = { repository.incrementJapCounter() },
                    onResetJap = { repository.resetJapCounter() },
                    onNavigateToDarshan = { navController.navigate("daily_darshan") },
                    onNavigateToLive = { navController.navigate("live_darshan") },
                    onNavigateToBhajans = { navController.navigate("bhajans") },
                    onNavigateToGallery = { navController.navigate("gallery") },
                    onNavigateToParayan = { navController.navigate("parayan") },
                    onNavigateToHistory = { navController.navigate("history") },
                    onNavigateToLocation = { navController.navigate("location") },
                    onNavigateToBhaktiMsg = { navController.navigate("bhakti_msg") },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToAdmin = { navController.navigate("admin") }
                )
            }

            // 2. Daily Darshan
            composable("daily_darshan") {
                DailyDarshanScreen(
                    dailyDarshan = dailyDarshan,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 3. Live Darshan
            composable("live_darshan") {
                LiveDarshanScreen(
                    liveConfig = liveConfig,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAdmin = { navController.navigate("admin") }
                )
            }

            // 4. Bhajans & Aarti
            composable("bhajans") {
                BhajanScreen(
                    bhajans = bhajans,
                    currentBhajan = currentBhajan,
                    isPlaying = isPlaying,
                    currentSec = currentSec,
                    durationSec = durationSec,
                    isLooping = isLooping,
                    onPlayBhajan = { bhajan ->
                        playerManager.playBhajan(bhajan, bhajans)
                    },
                    onTogglePlayPause = { playerManager.togglePlayPause() },
                    onPlayNext = { playerManager.playNext() },
                    onPlayPrev = { playerManager.playPrevious() },
                    onSeekTo = { sec -> playerManager.seekTo(sec) },
                    onToggleLoop = { playerManager.toggleLoop() },
                    onToggleFavorite = { id -> repository.toggleBhajanFavorite(id) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 5. Photo Gallery
            composable("gallery") {
                GalleryScreen(
                    galleryItems = galleryItems,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAdmin = { navController.navigate("admin") }
                )
            }

            // 6. Shri Gajanan Vijay Parayan
            composable("parayan") {
                ParayanScreen(
                    chapters = chapters,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 7. Temple History (Ghirni 'Prati Shegaon')
            composable("history") {
                TempleHistoryScreen(
                    historySections = historySections,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 8. Temple Location & Directions
            composable("location") {
                TempleLocationScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 9. Daily Bhakti Message & 365 Suvichar
            composable("bhakti_msg") {
                BhaktiMessageScreen(
                    messages = bhaktiMessages,
                    todayMarathiDate = todayMarathiDate,
                    todaySuvichar = todaySuvichar,
                    devoteeName = devoteeName,
                    onDevoteeNameChange = { repository.updateDevoteeName(it) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 10. Notifications
            composable("notifications") {
                NotificationsScreen(
                    notifications = notifications,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 11. Admin Dashboard
            composable("admin") {
                AdminDashboardScreen(
                    dailyDarshan = dailyDarshan,
                    liveConfig = liveConfig,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
