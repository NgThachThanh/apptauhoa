package com.example.apptauhoa.ui.ticket

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptauhoa.data.model.Seat
import com.example.apptauhoa.data.model.SeatStatus
import com.example.apptauhoa.data.model.TripDetails
import com.example.apptauhoa.ui.ticket.RailCarDisplayItem.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NavigationEvent(
    val tripId: String,
    val selectedSeatsInfo: String,
    val originalPrice: Long,
    val departureTime: Long,
    val arrivalTime: Long
)

class SeatSelectionViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val ticketCount: Int
    val coachType: String
    val coachName: String // ADDED

    private val _displayItems = MutableStateFlow<List<RailCarDisplayItem>>(emptyList())
    val displayItems = _displayItems.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _tripDetails = MutableStateFlow<TripDetails?>(null)
    val tripDetails = _tripDetails.asStateFlow()

    private var fullSeatList: List<Seat> = emptyList()
    private var selectedDeck = 1

    init {
        val debugCoachId = savedStateHandle.get<String>("coachId")
        ticketCount = savedStateHandle.get<Int>("ticketCount") ?: 1
        coachName = savedStateHandle.get<String>("coachName") ?: "" // ADDED

        if (!debugCoachId.isNullOrEmpty()) {
            coachType = if (debugCoachId.contains("SLEEPER")) "SLEEPER" else "SEAT"
            generateMockSeats(debugCoachId, coachType)
            transformSeatsToDisplayItems()
            _tripDetails.value = generateMockTripDetails(debugCoachId)
        } else {
            coachType = "UNKNOWN"
        }
    }

    private fun transformSeatsToDisplayItems() {
        val items = mutableListOf<RailCarDisplayItem>()
        val listToProcess = if (coachType == "SLEEPER") {
            fullSeatList.filter { it.deck == selectedDeck }
        } else {
            fullSeatList
        }

        if (listToProcess.isNotEmpty()) {
             when (coachType) {
                "SEAT" -> {
                    listToProcess.groupBy { it.rowNumber }
                        .toSortedMap()
                        .forEach { (rowNum, seats) ->
                            items.add(SeatRow(seats.sortedBy { it.positionInRow }))
                            if (rowNum % 4 == 0 && rowNum != 0) {
                                items.add(UtilitySpace("AISLE", rowNum))
                            }
                        }
                }
                "SLEEPER" -> {
                    listToProcess.groupBy { it.compartmentNumber }
                        .toSortedMap()
                        .forEach { (compNum, beds) ->
                            items.add(SleeperCompartment(beds.sortedBy { it.positionInRow }))
                        }
                }
            }
        }
        _displayItems.value = items
    }

    fun onSeatSelected(seat: Seat) {
        val targetSeat = fullSeatList.find { it.id == seat.id } ?: return

        if (targetSeat.status == SeatStatus.SELECTED) {
            val newList = fullSeatList.map {
                if (it.id == targetSeat.id) it.copy(status = SeatStatus.AVAILABLE) else it
            }
            fullSeatList = newList
            transformSeatsToDisplayItems()
            return
        }

        if (targetSeat.status == SeatStatus.AVAILABLE) {
            val selectedCount = fullSeatList.count { it.status == SeatStatus.SELECTED }

            if (selectedCount >= ticketCount) {
                viewModelScope.launch {
                    _uiEvent.emit("Bạn chỉ được chọn tối đa $ticketCount ghế.")
                }
                return
            }
            else {
                val newList = fullSeatList.map {
                    if (it.id == targetSeat.id) it.copy(status = SeatStatus.SELECTED) else it
                }
                fullSeatList = newList
                transformSeatsToDisplayItems()
                return
            }
        }
    }

    fun onDeckSelected(deck: Int) {
        selectedDeck = deck
        transformSeatsToDisplayItems()
    }

    fun processAndNavigate() {
        viewModelScope.launch {
            val selectedSeats = fullSeatList.filter { it.status == SeatStatus.SELECTED }
            if (selectedSeats.isEmpty()) {
                _uiEvent.emit("Vui lòng chọn ít nhất 1 ghế.")
                return@launch
            }

            val details = _tripDetails.value ?: return@launch

            val navArgs = NavigationEvent(
                tripId = details.tripId,
                selectedSeatsInfo = "$coachName: ${selectedSeats.joinToString { it.number }}", // MODIFIED
                originalPrice = selectedSeats.sumOf { it.price },
                departureTime = details.departureTime,
                arrivalTime = details.arrivalTime
            )
            _navigationEvent.emit(navArgs)
        }
    }

    private fun generateMockSeats(coachId: String, type: String) {
        val seats = mutableListOf<Seat>()
        val price = savedStateHandle.get<Long>("originalPrice") ?: 0L
         when (type) {
            "SEAT" -> {
                (1..15).forEach { row ->
                    listOf("A", "B", "C", "D").forEach { pos ->
                        val randomStatus = when { Math.random() > 0.75 -> SeatStatus.BOOKED else -> SeatStatus.AVAILABLE }
                        seats.add(Seat(id = "$coachId-$row-$pos", number = "$row$pos", status = randomStatus, price = price, seatType = "SEAT", rowNumber = row, positionInRow = pos, deck = 1, compartmentNumber = 0))
                    }
                }
            }
            "SLEEPER" -> {
                (1..4).forEach { compartment ->
                    listOf(1, 2).forEach { deck ->
                        listOf("1", "2").forEach { pos ->
                            val randomStatus = when { Math.random() > 0.75 -> SeatStatus.BOOKED else -> SeatStatus.AVAILABLE }
                            val bedNumber = "${compartment}${if (deck == 1) "A" else "B"}${pos}"
                            seats.add(Seat(
                                id = "$coachId-$compartment-$deck-$pos",
                                number = bedNumber,
                                status = randomStatus,
                                price = price,
                                seatType = "SLEEPER",
                                rowNumber = 0,
                                positionInRow = pos,
                                deck = deck,
                                compartmentNumber = compartment)
                            )
                        }
                    }
                }
            }
        }
        fullSeatList = seats
    }

    private fun generateMockTripDetails(coachId: String): TripDetails {
        val now = System.currentTimeMillis()
        val origin = savedStateHandle.get<String>("originStation") ?: "N/A"
        val destination = savedStateHandle.get<String>("destinationStation") ?: "N/A"
        return TripDetails(
            tripId = savedStateHandle.get<String>("tripId") ?: "TRIP123",
            trainCode = savedStateHandle.get<String>("trainCode") ?: "SE8",
            departureTime = now + 1000 * 60 * 180,
            arrivalTime = now + 1000 * 60 * 60 * 10,
            originStation = origin,
            destinationStation = destination,
            summary = "$origin - $destination"
        )
    }
}