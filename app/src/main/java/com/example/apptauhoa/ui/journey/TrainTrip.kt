package com.example.apptauhoa.ui.journey

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainTrip(
    val tripId: String,
    val trainCode: String,
    val coachClass: String,
    val availableSeats: Int,
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val originStation: String,
    val destinationStation: String,
    val price: Long,
    val tripDate: String
) : Parcelable