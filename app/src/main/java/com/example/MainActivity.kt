package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.ui.screens.BhaktiMessageScreen
import com.example.ui.screens.DailyDarshanScreen
import com.example.ui.screens.DailySuvicharScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.theme.GajananMaharajTheme

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

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // ---------------- HOME ----------------
        composable("home") {
            HomeScreen(
                onNavigateToDarshan = {
                    navController.navigate("darshan")
                },
                onNavigateToSuvichar = {
                    navController.navigate("suvichar")
                },
                onNavigateToBhaktiMessage = {
                    navController.navigate("bhakti_message")
                },
                onNavigateToHistory = {
                    navController.navigate("history")
                }
            )
        }

        // ---------------- DAILY DARSHAN ----------------
        composable("darshan") {
            DailyDarshanScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ---------------- DAILY SUVICHAR ----------------
        composable("suvichar") {
            DailySuvicharScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ---------------- BHAKTI MESSAGE ----------------
        composable("bhakti_message") {
            BhaktiMessageScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ---------------- HISTORY ----------------
        composable("history") {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
