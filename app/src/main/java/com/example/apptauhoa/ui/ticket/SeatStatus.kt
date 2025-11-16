package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SeatStatus : Parcelable {
    AVAILABLE,
    BOOKED,
    SELECTED
}