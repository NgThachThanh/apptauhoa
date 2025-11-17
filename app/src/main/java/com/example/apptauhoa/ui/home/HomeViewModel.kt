package com.example.apptauhoa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptauhoa.data.model.DestinationSuggestion
import com.example.apptauhoa.data.model.Promotion
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel : ViewModel() {

    // LiveData for the random suggestion card
    private val _randomSuggestion = MutableLiveData<DestinationSuggestion>()
    val randomSuggestion: LiveData<DestinationSuggestion> = _randomSuggestion
    
    // LiveData for promotions
    private val _promotions = MutableLiveData<List<Promotion>>()
    val promotions: LiveData<List<Promotion>> = _promotions

    // LiveData for the default departure date
    private val _defaultDepartureDate = MutableLiveData<Pair<Calendar, String>>()
    val defaultDepartureDate: LiveData<Pair<Calendar, String>> = _defaultDepartureDate


    // SharedFlow for one-time events, like button clicks
    private val _suggestionEvent = MutableSharedFlow<DestinationSuggestion>()
    val suggestionEvent = _suggestionEvent.asSharedFlow()

    init {
        loadRandomSuggestion()
        loadPromotions()
        loadDefaultDepartureDate()
    }

    private fun loadDefaultDepartureDate() {
        try {
            val today = Calendar.getInstance()
            val formatter = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale("vi", "VN"))
            val formattedDate = formatter.format(today.time)
            _defaultDepartureDate.value = Pair(today, formattedDate)
        } catch (e: Exception) {
            // Handle error, e.g., post a value that the fragment can interpret as "Choose date"
        }
    }
    
    /**
     * Loads a mocked list of promotions.
     */
    private fun loadPromotions() {
        val promotionList = listOf(
            Promotion("1", "Giảm 20% vé tàu Thống Nhất", ""),
            Promotion("2", "Du lịch hè sảng khoái, giá vé giảm ngay 15%", ""),
            Promotion("3", "Mua vé khứ hồi, giảm chiều về 30%", ""),
            Promotion("4", "Đi tàu 5 sao, trải nghiệm đẳng cấp", "")
        )
        _promotions.value = promotionList
    }


    /**
     * Loads a mocked random suggestion.
     * In a real app, this would come from a repository or use case.
     */
    fun loadRandomSuggestion() {
        // Mocked data as per the prompt
        val suggestion = DestinationSuggestion(
            stationName = "Đà Nẵng",
            stationCode = "DN",
            title = "Khám phá Thành phố đáng sống",
            imageUrl = "" // Placeholder, will be handled by a drawable
        )
        _randomSuggestion.value = suggestion
    }

    /**
     * Called when the user clicks the "Find Ticket" button on the suggestion card.
     * It emits an event to the fragment to perform UI actions.
     */
    fun onSuggestionCardClicked() {
        val suggestion = _randomSuggestion.value ?: return
        viewModelScope.launch {
            _suggestionEvent.emit(suggestion)
        }
    }
}
