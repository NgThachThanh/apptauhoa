package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seat(
    val id: String,
    val number: String,
    val status: SeatStatus,
    val deck: Int, // 1 for lower, 2 for upper
    val price: Long
) : Parcelable