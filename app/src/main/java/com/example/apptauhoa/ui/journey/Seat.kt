package com.example.apptauhoa.ui.journey

// Using the journey package to keep related models together for now
data class Seat(
    val seatId: String,
    val seatNumber: String,
    val status: SeatStatus,
    val price: Long,
    // Used to represent aisles or non-bookable spaces in the grid
    val isSelectable: Boolean = true 
)

enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    SOLD
}