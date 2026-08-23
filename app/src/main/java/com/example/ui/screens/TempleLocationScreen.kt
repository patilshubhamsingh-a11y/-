package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempleLocationScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Temple Location") }) }) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                                        Column(modifier = Modifier.padding(16.dp)) { Text("Shri Gajanan Maharaj Mandir, Shegaon - 444203") }
                                                    }
                                                            }
                                                                }
                                                                }