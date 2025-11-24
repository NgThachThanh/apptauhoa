package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seat(
    val id: String, // Unique ID for the seat, e.g., "COACH1-1A"
    val number: String, // Display number, e.g., "1A"
    val status: SeatStatus,
    val price: Long,
    val seatType: String, // "SEAT" or "SLEEPER"
    val coachName: String, // e.g., "Toa 1"

    // Properties for layout calculation
    val rowNumber: Int = 0,
    val positionInRow: String = "", // e.g., "A", "B"
    val deck: Int = 0, // 1 or 2 for sleepers
    val compartmentNumber: Int = 0 // For sleepers
) : Parcelable
