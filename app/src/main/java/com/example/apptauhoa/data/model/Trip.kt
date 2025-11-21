package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(
    val id: String,
    val trainCode: String,
    val classTitle: String, // Tương ứng coachClass
    val seatsLeft: Int,     // Tương ứng availableSeats
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val originStation: String,
    val destinationStation: String,
    val price: Long,        // Thống nhất dùng Long
    val tripDate: String    // Thêm từ TrainTrip
) : Parcelable