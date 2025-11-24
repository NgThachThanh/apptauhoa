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
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.SeatStatus
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
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeatSelectionBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val bookedSeatIds = dbHelper.getBookedSeatsForTrip(args.tripId).toMutableList()
        // Add some random booked seats for demonstration purposes
        bookedSeatIds.add("${args.coachId}-1A")
        bookedSeatIds.add("${args.coachId}-2C")
        bookedSeatIds.add("${args.coachId}-5B")

        viewModel.initialize(
            coachName = args.coachName,
            coachType = args.coachType,
            passengerCount = args.passengerCount,
            bookedSeatIds = bookedSeatIds.toSet(),
            price = args.originalPrice
        )

        setupAdapter()
        setupRecyclerView()
        setupObservers()

        binding.btnConfirmSeats.setOnClickListener {
            viewModel.processAndNavigate()
        }
    }

    private fun setupAdapter() {
        seatAdapter = SeatAdapter { seat ->
            viewModel.onSeatSelected(seat)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSeatmap.apply {
            adapter = seatAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.displayItems.collect { items ->
                        seatAdapter.submitList(items)

                        val allSeatsInCoach = items.flatMap { 
                            when (it) {
                                is RailCarDisplayItem.SeatRow -> it.seats
                                is RailCarDisplayItem.SleeperCompartment -> it.beds
                                else -> emptyList()
                            }
                        }
                        val selectedSeats = allSeatsInCoach.filter { it.status == SeatStatus.SELECTED }
                        val totalPrice = selectedSeats.sumOf { it.price }
                        val canProceed = selectedSeats.isNotEmpty() && selectedSeats.size <= args.passengerCount

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
                    viewModel.navigationEvent.collect { selectedSeats ->
                        val action = SeatSelectionFragmentDirections.actionSeatSelectionToPayment(
                            tripId = args.tripId,
                            trainCode = args.trainCode,
                            originStation = args.originStation,
                            destinationStation = args.destinationStation,
                            tripDate = args.tripDate,
                            departureTime = args.departureTime,
                            arrivalTime = args.arrivalTime,
                            selectedSeats = selectedSeats.toTypedArray()
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