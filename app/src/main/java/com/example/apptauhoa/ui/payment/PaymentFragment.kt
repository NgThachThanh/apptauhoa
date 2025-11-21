package com.example.apptauhoa.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentPaymentBinding
import com.example.apptauhoa.ui.ticket.BookedTicket
import com.example.apptauhoa.ui.ticket.TicketRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()
    private val args: PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSummary()
        setupListeners()
        setupObservers()

        if (savedInstanceState == null) {
            binding.radioGroupPayment.check(R.id.radio_cash)
        }
    }

    private fun setupSummary() {
        binding.textTripSummary.text = args.tripSummary
        binding.textSeatsInfo.text = args.selectedSeatsInfo
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.textFinalPrice.text = currencyFormat.format(args.originalPrice)
    }

    private fun setupListeners() {
        binding.radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            val method = when (checkedId) {
                R.id.radio_cash -> PaymentMethod.CASH_AT_COUNTER
                R.id.radio_qr -> PaymentMethod.ONLINE_QR
                else -> return@setOnCheckedChangeListener
            }
            viewModel.selectPaymentMethod(method)
        }

        binding.buttonConfirmBooking.setOnClickListener {
            val bookingCode = "#${Random.nextInt(1000, 9999)}"
            val bookedTicket = BookedTicket(
                tripSummary = args.tripSummary,
                selectedSeatsInfo = args.selectedSeatsInfo,
                originalPrice = args.originalPrice,
                departureTime = args.departureTime,
                arrivalTime = args.arrivalTime,
                tripId = args.tripId,
                bookingCode = bookingCode
            )
            TicketRepository.addTicket(bookedTicket)

            findNavController().navigate(R.id.action_payment_to_payment_success)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedPaymentMethod.collect { method ->
                    updatePaymentMethodUI(method)
                }
            }
        }
    }

    private fun updatePaymentMethodUI(method: PaymentMethod) {
        binding.layoutCashDetails.isVisible = method == PaymentMethod.CASH_AT_COUNTER
        binding.layoutQrDetails.isVisible = method == PaymentMethod.ONLINE_QR
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}