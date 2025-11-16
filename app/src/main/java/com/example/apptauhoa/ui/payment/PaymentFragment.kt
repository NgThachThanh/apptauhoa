package com.example.apptauhoa.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.databinding.FragmentPaymentBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        handleEvents()
        collectState()
    }

    private fun bindData() {
        binding.textTripSummary.text = viewModel.tripSummary
        binding.textSeatsInfo.text = viewModel.selectedSeatsInfo
    }

    private fun handleEvents() {
        binding.radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            val method = if (checkedId == binding.radioQr.id) PaymentMethod.QR else PaymentMethod.CASH
            viewModel.selectPaymentMethod(method)
        }

        binding.buttonApplyPromo.setOnClickListener {
            val code = binding.inputPromoCode.text.toString()
            viewModel.applyPromoCode(code)
        }

        binding.buttonConfirmBooking.setOnClickListener {
            // Navigate to the final ticket detail screen
            // val action = PaymentFragmentDirections.actionPaymentToTicketDetail()
            // findNavController().navigate(action)
            Toast.makeText(context, "Đặt vé thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe payment method selection
                launch {
                    viewModel.selectedMethod.collect { method ->
                        binding.layoutCashDetails.isVisible = method == PaymentMethod.CASH
                        binding.layoutQrDetails.isVisible = method == PaymentMethod.QR
                    }
                }
                
                // Observe cash deadline
                launch {
                    viewModel.cashPaymentDeadline.collect { deadline ->
                        binding.textCashWarning.text = deadline
                    }
                }

                // Observe final price
                launch {
                    viewModel.finalPrice.collect { price ->
                        binding.textFinalPrice.text = formatPrice(price)
                    }
                }

                // Observe UI events for toasts
                launch {
                    viewModel.uiEvent.collect { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun formatPrice(price: Long): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(price)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
