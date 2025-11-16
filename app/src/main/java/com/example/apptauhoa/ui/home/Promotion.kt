package com.example.apptauhoa.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promotion(
    val id: String,
    val title: String,
    val imageUrl: String // For now, can be a placeholder drawable name or a URL
) : Parcelable