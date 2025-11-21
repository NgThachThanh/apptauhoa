package com.example.apptauhoa.data.model

import androidx.annotation.DrawableRes

data class DestinationSuggestion(
    val stationName: String, 
    val stationCode: String, 
    val title: String,       
    @DrawableRes val imageResId: Int 
)
