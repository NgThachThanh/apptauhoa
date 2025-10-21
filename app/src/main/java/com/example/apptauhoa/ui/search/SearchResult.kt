package com.example.apptauhoa.ui.search

data class SearchResult(
    val trainName: String,
    val departureStation: String,
    val departureTime: String,
    val arrivalStation: String,
    val arrivalTime: String,
    val duration: String,
    val price: String
)