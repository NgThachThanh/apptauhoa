package com.example.apptauhoa.ui.ticket

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
    val totalPrice: Long // FIX: Ensure this is Long
) : Parcelable