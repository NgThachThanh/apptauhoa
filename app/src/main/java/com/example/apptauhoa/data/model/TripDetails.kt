package com.example.apptauhoa.data.model

data class TripDetails(
    val tripId: String,
    val trainCode: String,
    val departureTime: Long,
    val arrivalTime: Long,
    val originStation: String,
    val destinationStation: String,
    val summary: String
)