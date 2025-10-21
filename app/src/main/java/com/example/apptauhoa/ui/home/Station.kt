package com.example.apptauhoa.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Station(
    val code: String,
    val name: String
) : Parcelable