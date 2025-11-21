package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeatRef(
    val coachId: String,
    val seatNumber: String,
    val deck: Deck?
) : Parcelable

@Parcelize
data class SeatSelectionResult(
    val selectedSeats: List<SeatRef>,
    val totalPrice: Long
) : Parcelable