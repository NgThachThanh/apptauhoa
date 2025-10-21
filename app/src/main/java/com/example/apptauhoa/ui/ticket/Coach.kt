package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coach(
    val id: String,
    val name: String,
    val seats: List<Seat>
) : Parcelable

@Parcelize
data class Seat(
    val number: String,
    val status: SeatStatus,
    val deck: Deck? = null,
    val price: Int
) : Parcelable

enum class SeatStatus { AVAILABLE, SOLD, SELECTED, BLOCKED }
enum class Deck { UPPER, LOWER }