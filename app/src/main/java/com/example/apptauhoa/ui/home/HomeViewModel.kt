package com.example.apptauhoa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // For Promotions
    private val _promotions = MutableLiveData<List<Promotion>>()
    val promotions: LiveData<List<Promotion>> = _promotions

    // For Destination Suggestion Data
    private val _randomSuggestion = MutableLiveData<DestinationSuggestion>()
    val randomSuggestion: LiveData<DestinationSuggestion> = _randomSuggestion

    // For UI Events (Fill & Scroll Action)
    private val _suggestionEvent = MutableSharedFlow<DestinationSuggestion>()
    val suggestionEvent: SharedFlow<DestinationSuggestion> = _suggestionEvent.asSharedFlow()

    init {
        loadPromotions()
        loadRandomSuggestion()
    }

    fun onSuggestionCardClicked() {
        val suggestion = _randomSuggestion.value ?: return
        viewModelScope.launch {
            _suggestionEvent.emit(suggestion)
        }
    }

    private fun loadPromotions() {
        val mockList = listOf(
            Promotion("promo1", "Giảm 30% cho vé khứ hồi", "placeholder"),
            Promotion("promo2", "Đi Nha Trang chỉ từ 250.000đ", "placeholder"),
            Promotion("promo3", "Ưu đãi đặc biệt dịp lễ", "placeholder"),
            Promotion("promo4", "Miễn phí cho trẻ em dưới 6 tuổi", "placeholder")
        )
        _promotions.value = mockList
    }

    private fun loadRandomSuggestion() {
        // Mock a single suggestion
        _randomSuggestion.value = DestinationSuggestion(
            stationName = "Đà Nẵng",
            stationCode = "DN",
            title = "Khám phá Thành phố đáng sống",
            imageUrl = "placeholder"
        )
    }
}