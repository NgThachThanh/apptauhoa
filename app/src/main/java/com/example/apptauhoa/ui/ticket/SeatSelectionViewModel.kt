package com.example.apptauhoa.ui.ticket

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptauhoa.ui.ticket.RailCarDisplayItem.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NavigationEvent(
    val tripId: String,
    val tripSummary: String,
    val selectedSeatsInfo: String,
    val originalPrice: Long,
    val departureTime: Long
)

class SeatSelectionViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val ticketCount: Int
    private val coachType: String

    private val _displayItems = MutableStateFlow<List<RailCarDisplayItem>>(emptyList())
    val displayItems = _displayItems.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _tripDetails = MutableStateFlow<TripDetails?>(null)
    val tripDetails = _tripDetails.asStateFlow()

    private var fullSeatList: List<Seat> = emptyList()

    init {
        val debugCoachId = savedStateHandle.get<String>("coachId")
        ticketCount = savedStateHandle.get<Int>("ticketCount") ?: 1
        Log.d("SeatDebug", "ViewModel RECEIVED: coachId=$debugCoachId, ticketCount=$ticketCount")

        if (!debugCoachId.isNullOrEmpty()) {
            coachType = if (debugCoachId.startsWith("C05") || debugCoachId.startsWith("C06")) "SLEEPER" else "SEAT"
            generateMockSeats(debugCoachId, coachType)
            transformSeatsToDisplayItems()
            _tripDetails.value = generateMockTripDetails(debugCoachId)
        } else {
            coachType = "UNKNOWN"
            Log.e("SeatDebug", "FATAL ERROR: coachId is null or empty!")
        }
    }

    private fun transformSeatsToDisplayItems() {
        val items = mutableListOf<RailCarDisplayItem>()
        // Dummy implementation to avoid further errors, assuming this logic is correct
        if (fullSeatList.isNotEmpty()) {
             when (coachType) {
                "SEAT" -> {
                    fullSeatList.groupBy { it.rowNumber }
                        .toSortedMap()
                        .forEach { (rowNum, seats) ->
                            items.add(SeatRow(seats.sortedBy { it.positionInRow }))
                            if (rowNum % 4 == 0) {
                                items.add(UtilitySpace("AISLE", rowNum))
                            }
                        }
                }
                "SLEEPER" -> {
                    fullSeatList.groupBy { it.compartmentNumber }
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
        val selectedSeatCount = fullSeatList.count { it.status == SeatStatus.SELECTED }
        val targetSeatIndex = fullSeatList.indexOfFirst { it.id == seat.id }
        if (targetSeatIndex == -1) return

        val targetSeat = fullSeatList[targetSeatIndex]
        var stateDidChange = false

        when (targetSeat.status) {
            SeatStatus.AVAILABLE -> {
                if (selectedSeatCount < ticketCount) {
                    fullSeatList = fullSeatList.toMutableList().apply { this[targetSeatIndex] = targetSeat.copy(status = SeatStatus.SELECTED) }
                    stateDidChange = true
                } else {
                    viewModelScope.launch { _uiEvent.emit("Bạn đã chọn đủ $ticketCount vé.") }
                }
            }
            SeatStatus.SELECTED -> {
                fullSeatList = fullSeatList.toMutableList().apply { this[targetSeatIndex] = targetSeat.copy(status = SeatStatus.AVAILABLE) }
                stateDidChange = true
            }
            SeatStatus.BOOKED, SeatStatus.PENDING -> { /* Do nothing */ }
        }

        if (stateDidChange) {
            transformSeatsToDisplayItems()
        } else if (targetSeat.status == SeatStatus.AVAILABLE) {
            fullSeatList = fullSeatList.toMutableList().apply { this[targetSeatIndex] = targetSeat.copy(nonce = targetSeat.nonce + 1) }
            transformSeatsToDisplayItems()
        }
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
                tripSummary = details.summary,
                selectedSeatsInfo = "Toa ${savedStateHandle.get<String>("coachId")}: ${selectedSeats.joinToString { it.number }}",
                originalPrice = selectedSeats.sumOf { it.price },
                departureTime = details.departureTime
            )
            _navigationEvent.emit(navArgs)
        }
    }

    private fun generateMockSeats(coachId: String, type: String) {
        // Dummy implementation to avoid further errors
        val seats = mutableListOf<Seat>()
         when (type) {
            "SEAT" -> {
                (1..15).forEach { row ->
                    listOf("A", "B", "C", "D").forEach { pos ->
                        val randomStatus = when {
                            Math.random() > 0.9 -> SeatStatus.PENDING
                            Math.random() > 0.75 -> SeatStatus.BOOKED
                            else -> SeatStatus.AVAILABLE
                        }
                        seats.add(Seat(id = "$coachId-$row-$pos", number = "$row$pos", status = randomStatus, price = 350000L, seatType = "SEAT", rowNumber = row, positionInRow = pos, deck = 1))
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
            departureTime = now + 1000 * 60 * 180, // 3 hours from now
            arrivalTime = now + 1000 * 60 * 60 * 10, // 10 hours from now
            originStation = origin,
            destinationStation = destination,
            summary = "$origin - $destination"
        )
    }
}
