package com.example.apptauhoa.ui.journey

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coach(
    val coachId: String,
    val coachName: String,
    val coachType: String,
    val availableSeats: Int,
    val totalSeats: Int,
    val price: Long
) : Parcelable