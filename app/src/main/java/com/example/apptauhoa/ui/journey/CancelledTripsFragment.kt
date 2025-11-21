package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.databinding.FragmentCancelledTripsBinding
import com.example.apptauhoa.ui.ticket.BookedTicketAdapter
import com.example.apptauhoa.ui.ticket.TicketRepository

class CancelledTripsFragment : Fragment() {

    private var _binding: FragmentCancelledTripsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCancelledTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelledTickets = TicketRepository.bookedTickets.filter { it.status == "cancelled" }
        val adapter = BookedTicketAdapter(cancelledTickets) { ticket ->
            val action = JourneyFragmentDirections.actionJourneyToTicketDetails(ticket.tripId, null)
            findNavController().navigate(action)
        }

        binding.recyclerViewCancelledTrips.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCancelledTrips.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}