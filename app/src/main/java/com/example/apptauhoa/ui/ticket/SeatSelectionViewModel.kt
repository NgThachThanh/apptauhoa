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
            when {
                coachType.contains("Ngồi") -> {
                    fullSeatList.groupBy { it.rowNumber }
                        .toSortedMap()
                        .forEach { (rowNum, seats) ->
                            items.add(Header("Hàng $rowNum"))
                            items.add(SeatRow(seats.sortedBy { it.positionInRow }))
                        }
                }
                coachType.contains("Giường nằm") -> {
                     fullSeatList.groupBy { it.compartmentNumber }
                        .toSortedMap()
                        .forEach { (compartmentNum, beds) ->
                            items.add(Header("Khoang $compartmentNum"))
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
        val hardSeatPrice = (this.price * 0.8).toLong()
        val sleeper4Price = (this.price * 1.3).toLong()
        val sleeper6Price = (this.price * 1.2).toLong()

        when (type) {
            "Ngồi mềm điều hòa", "Ghế mềm điều hòa" -> {
                (1..16).forEach { row ->
                    listOf("A", "B", "C", "D").forEachIndexed { index, pos ->
                        val seatId = "$coachName-$row$pos"
                        val status = if (bookedSeatIds.contains(seatId)) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = seatId, number = "$row$pos", status = status, price = this.price, seatType = "SEAT", coachName = coachName, rowNumber = row, positionInRow = (index + 1).toString()))
                    }
                }
            }
            "Ngồi cứng điều hòa", "Ghế cứng điều hòa" -> {
                (1..20).forEach { row ->
                    listOf("A", "B", "C", "D").forEachIndexed { index, pos ->
                        val seatId = "$coachName-$row$pos"
                        val status = if (bookedSeatIds.contains(seatId)) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = seatId, number = "$row$pos", status = status, price = hardSeatPrice, seatType = "SEAT", coachName = coachName, rowNumber = row, positionInRow = (index + 1).toString()))
                    }
                }
            }
            "Giường nằm khoang 4" -> {
                (1..7).forEach { compartment ->
                    listOf("A", "B", "C", "D").forEachIndexed { index, pos ->
                        val seatId = "$coachName-$compartment$pos"
                        val status = if (bookedSeatIds.contains(seatId)) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = seatId, number = "$compartment$pos", status = status, price = sleeper4Price, seatType = "SLEEPER", coachName = coachName, compartmentNumber = compartment, positionInRow = (index + 1).toString()))
                    }
                }
            }
            "Giường nằm khoang 6" -> {
                 (1..7).forEach { compartment ->
                    listOf("A", "B", "C", "D", "E", "F").forEachIndexed { index, pos ->
                        val seatId = "$coachName-$compartment$pos"
                        val status = if (bookedSeatIds.contains(seatId)) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = seatId, number = "$compartment$pos", status = status, price = sleeper6Price, seatType = "SLEEPER", coachName = coachName, compartmentNumber = compartment, positionInRow = (index + 1).toString()))
                    }
                }
            }
        }
        fullSeatList = seats
    }
}
