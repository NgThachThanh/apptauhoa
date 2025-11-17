package com.example.apptauhoa.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentSeatSelectionBinding
import kotlinx.coroutines.launch

class SeatSelectionFragment : Fragment() {

    private var _binding: FragmentSeatSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeatSelectionViewModel by navGraphViewModels(R.id.navigation_seat_selection)
    
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

        setupToolbar()
        setupRecyclerView()
        setupConfirmationButton()
        
        collectUiState()
        collectUiEvents()
        collectNavigationEvents()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupRecyclerView() {
        seatAdapter = SeatAdapter { seat -> viewModel.onSeatSelected(seat) }
        binding.rvSeatmap.apply {
            adapter = seatAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    
    private fun setupConfirmationButton() {
        binding.btnConfirmSeats.setOnClickListener {
            viewModel.processAndNavigate()
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe seat map
                launch {
                    viewModel.displayItems.collect { items ->
                        seatAdapter.submitList(items)
                    }
                }
                // Observe trip details to update toolbar title
                launch {
                    viewModel.tripDetails.collect { details ->
                        if (details != null) {
                            binding.toolbar.title = "Chọn ghế - ${details.trainCode}"
                        }
                    }
                }
            }
        }
    }
    
     private fun collectNavigationEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { navArgs ->
                    val action = SeatSelectionFragmentDirections.actionSeatSelectionToPayment(
                        tripSummary = navArgs.tripSummary,
                        selectedSeatsInfo = navArgs.selectedSeatsInfo,
                        originalPrice = navArgs.originalPrice,
                        departureTime = navArgs.departureTime
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun collectUiEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
