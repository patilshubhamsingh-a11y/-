package com.example.data.model

data class DailySuvichar(
    val id: Int, // 1 to 366
    val month: Int, // 1 to 12
    val day: Int, // 1 to 31
    val suvichar: String,
    val category: String = "भक्ती व सदाचार"
)
