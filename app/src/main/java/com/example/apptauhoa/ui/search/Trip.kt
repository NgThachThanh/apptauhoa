package com.example.apptauhoa.ui.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(
    val id: String,
    val classTitle: String,
    val seatsLeft: Int,
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val originStation: String,
    val destinationStation: String,
    val price: String,
    val trainCode: String
) : Parcelable