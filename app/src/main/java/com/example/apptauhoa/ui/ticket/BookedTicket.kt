package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookedTicket(
    val tripSummary: String,
    val selectedSeatsInfo: String,
    val originalPrice: Long,
    val departureTime: Long,
    val arrivalTime: Long, // Added this field
    val tripId: String,
    val bookingCode: String
) : Parcelable
