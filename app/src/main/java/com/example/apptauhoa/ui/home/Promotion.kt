package com.example.apptauhoa.ui.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promotion(
    val id: String,
    val title: String,
    @DrawableRes val imageResId: Int,
    val description: String? = null // Add this line
) : Parcelable
