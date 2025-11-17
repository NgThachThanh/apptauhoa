package com.example.apptauhoa.data.model

import androidx.annotation.DrawableRes

data class DestinationSuggestion(
    val stationName: String, // Ví dụ: "Đà Nẵng"
    val stationCode: String, // Ví dụ: "DN" (để dùng sau)
    val title: String,       // Ví dụ: "Khám phá Thành phố đáng sống"
    val imageUrl: String     // (Dùng @drawable hoặc link placeholder)
)
