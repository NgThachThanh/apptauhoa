package com.example.apptauhoa.ui.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptauhoa.data.model.Seat
import com.example.apptauhoa.data.model.SeatStatus
import com.example.apptauhoa.ui.ticket.RailCarDisplayItem.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TripDetails(
    val summary: String
)

class SeatSelectionViewModel : ViewModel() {

    private var ticketCount: Int = 1
    private lateinit var coachType: String
    private lateinit var coachName: String
    private var price: Long = 0

    private val _displayItems = MutableStateFlow<List<RailCarDisplayItem>>(emptyList())
    val displayItems = _displayItems.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<List<Seat>>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _tripDetails = MutableStateFlow<TripDetails?>(null)
    val tripDetails = _tripDetails.asStateFlow()

    private var fullSeatList: List<Seat> = emptyList()

    fun initialize(coachName: String, coachType: String, passengerCount: Int, bookedSeatIds: Set<String>, price: Long) {
        this.coachName = coachName
        this.coachType = coachType
        this.ticketCount = passengerCount
        this.price = price

        generateMockSeats(coachName, coachType, bookedSeatIds)
        transformSeatsToDisplayItems()
        _tripDetails.value = TripDetails("$coachName | $coachType")
    }

    private fun transformSeatsToDisplayItems() {
        val items = mutableListOf<RailCarDisplayItem>()
        if (fullSeatList.isNotEmpty()) {
            when (coachType) {
                "Ngồi mềm điều hòa" -> {
                    fullSeatList.groupBy { it.rowNumber }
                        .toSortedMap()
                        .forEach { (_, seats) ->
                            items.add(SeatRow(seats.sortedBy { it.positionInRow }))
                        }
                }
                "Giường nằm khoang 4" -> {
                     fullSeatList.groupBy { it.compartmentNumber }
                        .toSortedMap()
                        .forEach { (_, beds) ->
                            items.add(SleeperCompartment(beds.sortedBy { it.positionInRow }))
                        }
                }
            }
        }
        _displayItems.value = items
    }

    fun onSeatSelected(seat: Seat) {
        val targetSeat = fullSeatList.find { it.id == seat.id } ?: return

        if (targetSeat.status == SeatStatus.BOOKED) {
            viewModelScope.launch { _uiEvent.emit("Ghế này đã được đặt.") }
            return
        }

        if (targetSeat.status == SeatStatus.SELECTED) {
            updateSeatStatus(targetSeat.id, SeatStatus.AVAILABLE)
            return
        }

        if (targetSeat.status == SeatStatus.AVAILABLE) {
            val selectedCount = fullSeatList.count { it.status == SeatStatus.SELECTED }

            if (selectedCount >= ticketCount) {
                viewModelScope.launch {
                    _uiEvent.emit("Bạn chỉ được chọn tối đa $ticketCount ghế.")
                }
                return
            } else {
                updateSeatStatus(targetSeat.id, SeatStatus.SELECTED)
            }
        }
    }
    
    private fun updateSeatStatus(seatId: String, newStatus: SeatStatus) {
        val newList = fullSeatList.map {
            if (it.id == seatId) it.copy(status = newStatus) else it
        }
        fullSeatList = newList
        transformSeatsToDisplayItems()
    }

    fun processAndNavigate() {
        viewModelScope.launch {
            val selectedSeats = fullSeatList.filter { it.status == SeatStatus.SELECTED }
            if (selectedSeats.isEmpty()) {
                _uiEvent.emit("Vui lòng chọn ít nhất 1 ghế.")
                return@launch
            }
            _navigationEvent.emit(selectedSeats)
        }
    }

    private fun generateMockSeats(coachName: String, type: String, bookedSeatIds: Set<String>) {
        val seats = mutableListOf<Seat>()
        val sleeperPrice = (this.price * 1.3).toLong()
        when (type) {
            "Ngồi mềm điều hòa" -> {
                (1..15).forEach { row ->
                    listOf("A", "B", "C", "D").forEach { pos ->
                        val seatId = "$coachName-$row$pos"
                        val status = if (bookedSeatIds.contains(seatId)) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = seatId, number = "$row$pos", status = status, price = this.price, seatType = "SEAT", coachName = coachName, rowNumber = row, positionInRow = pos))
                    }
                }
            }
            "Giường nằm khoang 4" -> {
                (1..7).forEach { compartment ->
                    listOf("A", "B", "C", "D").forEach { pos ->
                        val seatId = "$coachName-$compartment$pos"
                        val status = if (bookedSeatIds.contains(seatId)) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = seatId, number = "$compartment$pos", status = status, price = sleeperPrice, seatType = "SLEEPER", coachName = coachName, compartmentNumber = compartment, positionInRow = pos))
                    }
                }
            }
        }
        fullSeatList = seats
    }
}