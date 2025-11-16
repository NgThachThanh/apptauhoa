package com.example.apptauhoa.ui.journey

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PaymentMethod {
    CASH_AT_COUNTER,
    ONLINE_QR
}

class PaymentViewModel : ViewModel() {

    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.CASH_AT_COUNTER)
    val selectedPaymentMethod: StateFlow<PaymentMethod> = _selectedPaymentMethod.asStateFlow()

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }
}