package com.example.apptauhoa.ui.journey

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class SelectionSummary(
    val selectedSeats: List<String> = emptyList(),
    val totalPrice: String = "0đ"
)

class SeatSelectionViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val coachId: String = savedStateHandle.get<String>("coachId")!!
    private val passengerLimit: Int = savedStateHandle.get<Int>("totalPassengers")!!

    private val _seatMap = MutableStateFlow<List<Seat>>(emptyList())
    val seatMap: StateFlow<List<Seat>> = _seatMap.asStateFlow()

    private val _selectionSummary = MutableStateFlow(SelectionSummary())
    val selectionSummary: StateFlow<SelectionSummary> = _selectionSummary.asStateFlow()
    
    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()
    
    val spanCount: Int = if (coachId.contains("Sleeper")) 4 else 5

    init {
        loadSeatMap()
    }

    fun onSeatSelected(seatId: String) {
        val currentSeats = _seatMap.value
        val seat = currentSeats.firstOrNull { it.seatId == seatId } ?: return

        if (seat.status == SeatStatus.SOLD || !seat.isSelectable) return

        val selectedCount = currentSeats.count { it.status == SeatStatus.SELECTED }

        if (seat.status == SeatStatus.AVAILABLE && selectedCount >= passengerLimit) {
            viewModelScope.launch {
                _errorEvent.emit("Bạn đã chọn đủ số ghế cho $passengerLimit hành khách.")
            }
            return
        }
        
        val newStatus = if (seat.status == SeatStatus.AVAILABLE) SeatStatus.SELECTED else SeatStatus.AVAILABLE
        val updatedSeats = currentSeats.map {
            if (it.seatId == seatId) it.copy(status = newStatus) else it
        }
        
        _seatMap.value = updatedSeats
        updateSelectionSummary()
    }

    private fun updateSelectionSummary() {
        // ... (this function is unchanged)
    }

    private fun loadSeatMap() {
        // ... (this function is unchanged)
    }

    // --- Mock Data Generation ---
    // ... (these functions are unchanged)
}