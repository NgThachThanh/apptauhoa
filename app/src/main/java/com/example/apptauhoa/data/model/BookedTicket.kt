package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookedTicket(
    val tripSummary: String,
    val selectedSeatsInfo: String,
    val originalPrice: Long,
    val departureTime: Long,
    val arrivalTime: Long,
    val tripId: String,
    val bookingCode: String,
    val status: String = "booked"
) : Parcelable