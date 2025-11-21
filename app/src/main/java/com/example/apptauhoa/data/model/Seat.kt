package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seat(
    val id: String,
    val number: String,
    var status: SeatStatus,
    val price: Long,
    val seatType: String,
    val rowNumber: Int = 0,
    val compartmentNumber: Int = 0,
    val positionInRow: String,
    val deck: Int,
    val nonce: Int = 0
) : Parcelable