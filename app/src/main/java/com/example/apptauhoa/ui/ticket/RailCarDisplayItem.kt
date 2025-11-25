package com.example.apptauhoa.ui.ticket

import com.example.apptauhoa.data.model.Seat

sealed class RailCarDisplayItem {
    abstract val key: String

    data class Header(val title: String) : RailCarDisplayItem() {
        override val key: String = title
    }

    data class SeatRow(val seats: List<Seat>) : RailCarDisplayItem() {
        override val key: String = "row-${seats.first().rowNumber}"
    }

    data class SleeperCompartment(val beds: List<Seat>) : RailCarDisplayItem() {
        override val key: String = "sleeper-${beds.first().compartmentNumber}"
    }

    data class UtilitySpace(val type: String) : RailCarDisplayItem() {
        override val key: String = "util-$type"
    }
}
