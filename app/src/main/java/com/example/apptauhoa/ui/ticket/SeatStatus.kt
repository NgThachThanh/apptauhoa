package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SeatStatus : Parcelable {
    AVAILABLE,
    SELECTED,
    BOOKED, // Note: The prompt uses SOLD, but the codebase uses BOOKED. Sticking to BOOKED for consistency.
    PENDING
}
