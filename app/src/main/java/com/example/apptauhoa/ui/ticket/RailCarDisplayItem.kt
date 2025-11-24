package com.example.apptauhoa.ui.ticket

import com.example.apptauhoa.data.model.Seat

sealed class RailCarDisplayItem {
    data class SeatRow(
        val seats: List<Seat>,
        // Use the unique ID of the first seat as the key for the row
        val key: String = seats.first().id 
    ) : RailCarDisplayItem()

    data class SleeperCompartment(
        val beds: List<Seat>,
        // Use the unique ID of the first bed as the key for the compartment
        val key: String = beds.first().id 
    ) : RailCarDisplayItem()

    data class UtilitySpace(
        val type: String, // e.g., AISLE, TOILET
        // Create a unique key based on type and a random element
        val key: String = "${type}_${System.nanoTime()}" 
    ) : RailCarDisplayItem()
}