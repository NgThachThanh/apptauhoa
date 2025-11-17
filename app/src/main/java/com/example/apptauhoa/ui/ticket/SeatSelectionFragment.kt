package com.example.apptauhoa.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apptauhoa.databinding.FragmentSeatSelectionBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class SeatSelectionFragment : Fragment() {

    private var _binding: FragmentSeatSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeatSelectionViewModel by viewModels()
    private val args: SeatSelectionFragmentArgs by navArgs()
    private lateinit var seatAdapter: SeatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeatSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupObservers()

        binding.btnConfirmSeats.setOnClickListener {
            viewModel.processAndNavigate()
        }
    }

    private fun setupAdapter() {
        seatAdapter = SeatAdapter { seat ->
            val allItems = seatAdapter.currentList
            val allSeatsInCoach = allItems.flatMap {
                when (it) {
                    is RailCarDisplayItem.SeatRow -> it.seats
                    is RailCarDisplayItem.SleeperCompartment -> it.beds
                    else -> emptyList()
                }
            }
            val selectedSeats = allSeatsInCoach.filter { it.status == SeatStatus.SELECTED }

            if (args.ticketCount == 1 && selectedSeats.isNotEmpty() && seat.status != SeatStatus.SELECTED) {
                Toast.makeText(requireContext(), "Bạn chỉ được chọn 1 ghế", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.onSeatSelected(seat)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvSeatmap.apply {
            adapter = seatAdapter
            // Each item in the new adapter (SeatRow, SleeperCompartment) represents a full row.
            // So a GridLayoutManager with a spanCount of 1 is correct.
            layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.displayItems.collect { items ->
                        seatAdapter.submitList(items)

                        // --- Update UI based on the new list of items ---
                        val allSeatsInCoach = items.flatMap {
                            when (it) {
                                is RailCarDisplayItem.SeatRow -> it.seats
                                is RailCarDisplayItem.SleeperCompartment -> it.beds
                                else -> emptyList()
                            }
                        }
                        val selectedSeats = allSeatsInCoach.filter { it.status == SeatStatus.SELECTED }
                        val totalPrice = selectedSeats.sumOf { it.price }
                        val canProceed = selectedSeats.isNotEmpty() && selectedSeats.size <= args.ticketCount

                        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                        binding.txtTotalPrice.text = currencyFormat.format(totalPrice)

                        val selectedSummary = if (selectedSeats.isEmpty()) {
                            "Chưa chọn ghế"
                        } else {
                            "Đã chọn: ${selectedSeats.joinToString { it.number }}"
                        }
                        binding.txtSelectedSummary.text = selectedSummary

                        binding.btnConfirmSeats.isEnabled = canProceed
                    }
                }

                launch {
                    viewModel.uiEvent.collect { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    viewModel.navigationEvent.collect { navEvent ->
                        val action = SeatSelectionFragmentDirections.actionSeatSelectionToPayment(
                            tripSummary = navEvent.tripSummary,
                            selectedSeatsInfo = navEvent.selectedSeatsInfo,
                            originalPrice = navEvent.originalPrice,
                            departureTime = navEvent.departureTime,
                            arrivalTime = navEvent.arrivalTime,
                            tripId = navEvent.tripId
                        )
                        findNavController().navigate(action)
                    }
                }

                launch {
                    viewModel.tripDetails.collect { details ->
                        binding.txtSubtitle.text = details?.summary ?: ""
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}