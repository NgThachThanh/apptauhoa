package com.example.apptauhoa.ui.journey

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
        
        binding.btnContinue.setOnClickListener {
            navigateToPayment()
        }
    }
    
    private fun navigateToPayment() {
        lifecycleScope.launch {
            val summary = viewModel.selectionSummary.first()
            
            val action = SeatSelectionFragmentDirections.actionSeatSelectionToPayment(
                tripSummary = "Sài Gòn - Nha Trang, 15/11/2025", // Placeholder
                selectedSeatsInfo = "Ghế: ${summary.selectedSeats.joinToString(", ")} (Toa ${args.coachId})",
                totalPrice = summary.totalPrice.filter { it.isDigit() }.toLongOrNull() ?: 0L
            )
            findNavController().navigate(action)
        }
    }

    private fun setupAdapter() {
        seatAdapter = SeatAdapter { seatId ->
            viewModel.onSeatSelected(seatId)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSeatMap.apply {
            adapter = seatAdapter
            layoutManager = GridLayoutManager(requireContext(), viewModel.spanCount)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.seatMap.collect { seats ->
                        seatAdapter.submitList(seats)
                    }
                }
                
                launch {
                    viewModel.selectionSummary.collect { summary ->
                        binding.txtSelectedSeatsValue.text = if (summary.selectedSeats.isEmpty()) "Chưa chọn ghế" else summary.selectedSeats.joinToString(", ")
                        binding.txtTotalPrice.text = summary.totalPrice
                        binding.btnContinue.isEnabled = summary.selectedSeats.isNotEmpty()
                    }
                }
                
                launch {
                    viewModel.errorEvent.collect { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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