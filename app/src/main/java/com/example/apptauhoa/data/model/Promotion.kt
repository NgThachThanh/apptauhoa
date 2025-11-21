package com.example.apptauhoa.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promotion(
    val id: String,
    val title: String,
    @DrawableRes val imageResId: Int
) : Parcelable