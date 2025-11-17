package com.example.apptauhoa.ui.ticket

// A simple data class to hold detailed trip info fetched by the ViewModel
data class TripDetails(
    val tripId: String,
    val trainCode: String,
    val departureTime: Long,
    val arrivalTime: Long,
    val originStation: String,
    val destinationStation: String,
    val summary: String
)
