package com.example.apptauhoa.ui.journey

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
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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
    }

    private fun setupSummary() {
        binding.textViewTripSummary.text = args.tripSummary
        binding.textViewSeatsInfo.text = args.selectedSeatsInfo
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.textViewTotalPrice.text = currencyFormat.format(args.totalPrice)
    }

    private fun setupListeners() {
        binding.radioGroupPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_cash -> viewModel.selectPaymentMethod(PaymentMethod.CASH_AT_COUNTER)
                R.id.radio_button_qr -> viewModel.selectPaymentMethod(PaymentMethod.ONLINE_QR)
            }
        }

        binding.buttonConfirmPayment.setOnClickListener {
            findNavController().navigate(R.id.action_payment_to_ticket_detail)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedPaymentMethod.collect { method ->
                    binding.textViewCashInstructions.isVisible = method == PaymentMethod.CASH_AT_COUNTER
                    binding.imageViewQrCode.isVisible = method == PaymentMethod.ONLINE_QR
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}