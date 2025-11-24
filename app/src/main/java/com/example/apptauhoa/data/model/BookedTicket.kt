package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookedTicket(
    val selectedSeatsInfo: String,
    val originalPrice: Long,
    val departureTime: Long,
    val arrivalTime: Long,
    val tripId: String,
    val bookingCode: String,
    val trainCode: String,
    val originStation: String,
    val destinationStation: String,
    val tripDate: String,
    val status: String = "booked"
) : Parcelable