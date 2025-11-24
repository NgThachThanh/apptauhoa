package com.example.apptauhoa.ui.payment

import android.content.Context
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
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.databinding.FragmentPaymentBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()
    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

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
        val summary = "${args.trainCode} | ${args.originStation} -> ${args.destinationStation} | ${args.tripDate}"
        binding.textTripSummary.text = summary
        binding.textSeatsInfo.text = args.selectedSeatsInfo
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.textFinalPrice.text = currencyFormat.format(args.originalPrice)
    }

    private fun setupListeners() {
        binding.buttonConfirmBooking.setOnClickListener {
            val bookingCode = "#${Random.nextInt(1000, 9999)}"
            val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("USER_ID", -1)

            val bookedTicket = BookedTicket(
                selectedSeatsInfo = args.selectedSeatsInfo,
                originalPrice = args.originalPrice, // This is the total price
                departureTime = args.departureTime,
                arrivalTime = args.arrivalTime,
                tripId = args.tripId,
                bookingCode = bookingCode,
                trainCode = args.trainCode,
                originStation = args.originStation,
                destinationStation = args.destinationStation,
                tripDate = args.tripDate,
                status = "BOOKED"
            )
            // Use DatabaseHelper to save the ticket
            dbHelper.addTicket(bookedTicket, userId)

            findNavController().navigate(R.id.action_payment_to_payment_success)
        }

        binding.radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            val method = when (checkedId) {
                R.id.radio_cash -> PaymentMethod.CASH_AT_COUNTER
                R.id.radio_qr -> PaymentMethod.ONLINE_QR
                else -> return@setOnCheckedChangeListener
            }
            viewModel.selectPaymentMethod(method)
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