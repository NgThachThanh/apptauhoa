package com.example.apptauhoa.ui.ticket

import com.example.apptauhoa.ui.ticket.Seat

/**
 * Represents a displayable item in the seat selection RecyclerView.
 * This allows for multiple view types (rows, compartments, utility spaces).
 */
sealed interface RailCarDisplayItem {
    // A unique ID for DiffUtil
    val id: String

    data class SeatRow(val seats: List<Seat>) : RailCarDisplayItem {
        override val id: String = seats.firstOrNull()?.id?.substringBeforeLast('-') ?: "row_${seats.hashCode()}"
    }

    data class SleeperCompartment(val beds: List<Seat>) : RailCarDisplayItem {
        override val id: String = beds.firstOrNull()?.id?.substringBeforeLast('-') ?: "comp_${beds.hashCode()}"
    }

    data class UtilitySpace(val type: String, val key: Int) : RailCarDisplayItem {
        override val id: String = "${type}_$key"
    }
}
