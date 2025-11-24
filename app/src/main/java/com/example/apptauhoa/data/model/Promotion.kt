package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promotion(
    val id: Int = 0,
    val code: String,
    val description: String,
    val discountPercent: Int,
    val imageResId: String
) : Parcelable
