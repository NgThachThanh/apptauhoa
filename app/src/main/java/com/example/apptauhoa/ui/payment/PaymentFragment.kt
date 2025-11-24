package com.example.apptauhoa.ui.payment

import android.content.Context
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

    private var originalPrice: Long = 0
    private var finalPrice: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        
        // Calculate original price from the list of seats passed as argument
        originalPrice = args.selectedSeats.sumOf { it.price }
        finalPrice = originalPrice
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
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.textTripSummary.text = "${args.trainCode} | ${args.originStation} -> ${args.destinationStation} | ${args.tripDate}"
        
        // Generate seat info string from the list of seats
        val seatsInfo = args.selectedSeats.joinToString(", ") { it.number }
        binding.textSeatsInfo.text = seatsInfo
        
        binding.textOriginalPrice.text = currencyFormat.format(originalPrice)
        binding.textFinalPrice.text = currencyFormat.format(finalPrice)
        
        // Hide discount fields initially
        binding.labelDiscount.isVisible = false
        binding.textDiscountAmount.isVisible = false
    }

    private fun setupListeners() {
        binding.buttonApplyPromo.setOnClickListener {
            applyPromoCode()
        }

        binding.buttonConfirmBooking.setOnClickListener {
            confirmBooking()
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

    private fun applyPromoCode() {
        val code = binding.editTextPromoCode.text.toString().trim()
        if (code.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập mã khuyến mãi", Toast.LENGTH_SHORT).show()
            return
        }

        val promo = dbHelper.getPromotionByCode(code)
        if (promo == null) {
            Toast.makeText(requireContext(), "Mã khuyến mãi không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        val discountPercent = promo.discountPercent
        val discountAmount = (originalPrice * discountPercent) / 100
        finalPrice = originalPrice - discountAmount

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.textDiscountAmount.text = "- ${currencyFormat.format(discountAmount)}"
        binding.textFinalPrice.text = currencyFormat.format(finalPrice)

        binding.labelDiscount.isVisible = true
        binding.textDiscountAmount.isVisible = true
        
        Toast.makeText(requireContext(), "Áp dụng mã thành công!", Toast.LENGTH_SHORT).show()
    }

    private fun confirmBooking() {
        val bookingCode = "#${Random.nextInt(1000, 9999)}"
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)
        val seatsInfo = args.selectedSeats.joinToString(", ") { it.number }

        val bookedTicket = BookedTicket(
            selectedSeatsInfo = seatsInfo,
            originalPrice = finalPrice, // Use the final price after discount
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

        // Correctly call addTicket with the list of seats
        dbHelper.addTicket(bookedTicket, userId, args.selectedSeats.toList())
        findNavController().navigate(R.id.action_payment_to_payment_success)
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
        binding.layoutCashDetails.root.isVisible = method == PaymentMethod.CASH_AT_COUNTER
        binding.layoutQrDetails.root.isVisible = method == PaymentMethod.ONLINE_QR
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}