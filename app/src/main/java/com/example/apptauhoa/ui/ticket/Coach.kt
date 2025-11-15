package com.example.apptauhoa.ui.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coach(
    val id: String,
    val name: String,
    val seats: List<Seat>
) : Parcelable
