package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempleHistoryScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Temple History") }) }) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                                            Text("Shri Gajanan Maharaj Mandir History", style = MaterialTheme.typography.titleLarge)
                                                                                Spacer(modifier = Modifier.height(8.dp))
                                                                                                    Text("Shegaon is holy place of Shri Gajanan Maharaj. Temple built in 1908.")
                                                                                                                    }
                                                                                                                                }
                                                                                                                                        }
                                                                                                                                            }
                                                                                                                                            }