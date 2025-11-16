package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seat(
    val id: String, // A unique ID for the seat, e.g., "C01-1-A1"
    val number: String, // The display number, e.g., "A1", "Giường 3"
    var status: SeatStatus,
    val price: Long,

    // --- NEW PROPERTIES ---
    val seatType: String, // "SEAT" or "SLEEPER"
    val rowNumber: Int = 0, // Row for seated cars
    val compartmentNumber: Int = 0, // Compartment for sleeper cars
    val positionInRow: String, // e.g., "A", "B" for seats; "T1L" (Tầng 1 Trái) for sleepers
    val deck: Int, // 1 for lower, 2 for upper

    // A dummy property to trick DiffUtil into re-binding the view
    val nonce: Int = 0
) : Parcelable
