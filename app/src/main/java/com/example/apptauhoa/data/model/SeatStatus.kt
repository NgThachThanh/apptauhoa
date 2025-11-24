package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SeatStatus : Parcelable {
    AVAILABLE,
    SELECTED,
    BOOKED
}