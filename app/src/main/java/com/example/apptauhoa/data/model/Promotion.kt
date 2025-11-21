package com.example.apptauhoa.data.model

import androidx.annotation.DrawableRes

data class Promotion(
    val id: String,
    val title: String,
    @DrawableRes val imageResId: Int
)
