package com.example.apptauhoa.ui.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class PaymentMethod { CASH, QR }

class PaymentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    // --- Args from Navigation ---
    val tripSummary: String = savedStateHandle.get<String>("tripSummary") ?: ""
    val selectedSeatsInfo: String = savedStateHandle.get<String>("selectedSeatsInfo") ?: ""
    private val originalPrice: Long = savedStateHandle.get<Long>("originalPrice") ?: 0L
    private val departureTime: Long = savedStateHandle.get<Long>("departureTime") ?: 0L

    // --- UI State ---
    private val _selectedMethod = MutableStateFlow(PaymentMethod.CASH)
    val selectedMethod = _selectedMethod.asStateFlow()

    private val _cashPaymentDeadline = MutableStateFlow("")
    val cashPaymentDeadline = _cashPaymentDeadline.asStateFlow()

    private val _discount = MutableStateFlow(0L)
    val discount = _discount.asStateFlow()

    val finalPrice: StateFlow<Long> = discount.map { discountAmount ->
        (originalPrice - discountAmount).coerceAtLeast(0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), originalPrice)
    
    // --- One-time Events ---
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        calculateCashDeadline()
    }

    private fun calculateCashDeadline() {
        if (departureTime > 0) {
            val deadlineTime = departureTime - (30 * 60 * 1000) // 30 minutes in millis
            val formatter = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale("vi", "VN"))
            _cashPaymentDeadline.value = "Vui lòng thanh toán trước ${formatter.format(Date(deadlineTime))}"
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedMethod.value = method
        // Reset discount if switching away from QR payment
        if (method != PaymentMethod.QR) {
            _discount.value = 0L
        }
    }

    fun applyPromoCode(code: String) {
        viewModelScope.launch {
            if (code.equals("SALE50", ignoreCase = true)) {
                _discount.value = 50000L
                _uiEvent.emit("Áp dụng mã giảm giá thành công!")
            } else {
                _discount.value = 0L
                _uiEvent.emit("Mã giảm giá không hợp lệ.")
            }
        }
    }
}
