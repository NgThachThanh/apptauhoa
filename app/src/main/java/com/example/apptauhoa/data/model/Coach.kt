package com.example.apptauhoa.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coach(
    val id: String,         // coachId
    val name: String,       // coachName
    val type: String,       // coachType
    val availableSeats: Int,
    val totalSeats: Int,
    val price: Long,
    val seats: List<Seat> = emptyList() // Thêm từ ui.ticket.Coach
) : Parcelable