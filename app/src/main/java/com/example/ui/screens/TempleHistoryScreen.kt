package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempleHistoryScreen() {
    Scaffold(
            topBar = { TopAppBar(title = { Text("Temple History") }) }
                ) { padding ->
                        Column(
                                    modifier = Modifier
                                                    .fillMaxSize()
                                                                    .padding(padding)
                                                                                    .verticalScroll(rememberScrollState())
                                                                                                    .padding(16.dp),
                                                                                                                verticalArrangement = Arrangement.spacedBy(16.dp)
                                                                                                                        ) {
                                                                                                                                    Card(
                                                                                                                                                    modifier = Modifier.fillMaxWidth(),
                                                                                                                                                                    shape = RoundedCornerShape(16.dp),
                                                                                                                                                                                    elevation = CardDefaults.cardElevation(4.dp)
                                                                                                                                                                                                ) {
                                                                                                                                                                                                                Column(modifier = Modifier.padding(16.dp)) {
                                                                                                                                                                                                                                    Text("Shri Gajanan Maharaj Mandir", style = MaterialTheme.typography.titleLarge)
                                                                                                                                                                                                                                                        Spacer(modifier = Modifier.height(8.dp))
                                                                                                                                                                                                                                                                            Text("Shegaon is a holy place of Shri Gajanan Maharaj. The temple was built in 1908. Millions of devotees visit every year. This temple is known for its spiritual peace and history.")
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                    Card(
                                                                                                                                                                                                                                                                                                                                    modifier = Modifier.fillMaxWidth(),
                                                                                                                                                                                                                                                                                                                                                    shape = RoundedCornerShape(16.dp)
                                                                                                                                                                                                                                                                                                                                                                ) {
                                                                                                                                                                                                                                                                                                                                                                                Column(modifier = Modifier.padding(16.dp)) {
                                                                                                                                                                                                                                                                                                                                                                                                    Text("History: Gajanan Maharaj appeared in Shegaon in 1878. His samadhi is here. The temple trust manages all facilities.")
                                                                                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                                                                                                                            }