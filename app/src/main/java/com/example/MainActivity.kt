package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.local.DevotionalDataStore
import com.example.data.local.suvichar.DailySuvicharManager
import com.example.ui.screens.BhaktiMessageScreen
import com.example.ui.screens.DailyDarshanScreen
import com.example.ui.screens.DailySuvicharScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.theme.GajananMaharajTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GajananMaharajTheme {
                GajananMaharajApp()
            }
        }
    }
}

@Composable
fun GajananMaharajApp() {
    val navController = rememberNavController()
    var devoteeName by remember { mutableStateOf("") }
    
    val calendar = remember { Calendar.getInstance() }
    val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
    val todayMonth = calendar.get(Calendar.MONTH) + 1

    val todaySuvicharItem = remember(todayDay, todayMonth) {
        DailySuvicharManager.allSuvichars.firstOrNull {
            it.day == todayDay && it.month == todayMonth
        }
    }
    val todaySuvichar = todaySuvicharItem?.suvichar
        ?: "श्री गजानन महाराजांची कृपा सदैव आपल्या सर्वांवर राहो."
    val todayMarathiDate = if (todaySuvicharItem != null) {
        "${DailySuvicharManager.toMarathiDigits(todaySuvicharItem.day)} " +
                DailySuvicharManager.getMarathiMonthName(todaySuvicharItem.month)
    } else {
        "आजचे पावन दर्शन"
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                unreadNotifCount = 0,
                todayMarathiDate = todayMarathiDate,
                todaySuvichar = todaySuvichar,
                devoteeName = devoteeName,
                onDevoteeNameChange = { newName -> devoteeName = newName },
                onNavigateToDarshan = { navController.navigate("darshan") },
                onNavigateToSuvichar = { navController.navigate("suvichar") },
                onNavigateToBhaktiMessage = { navController.navigate("bhakti_message") },
                onNavigateToHistory = { navController.navigate("history") }
            )
        }
        composable("darshan") { DailyDarshanScreen(onNavigateBack = { navController.popBackStack() }) }
        composable("suvichar") { DailySuvicharScreen(onNavigateBack = { navController.popBackStack() }) }
        composable("bhakti_message") {
            BhaktiMessageScreen(
                messages = DevotionalDataStore.dailyBhaktiMessages,
                todayMarathiDate = todayMarathiDate,
                todaySuvichar = todaySuvichar,
                devoteeName = devoteeName,
                onDevoteeNameChange = { newName -> devoteeName = newName },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("history") {
            HistoryScreen(
                historySections = DevotionalDataStore.templeHistorySections,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
