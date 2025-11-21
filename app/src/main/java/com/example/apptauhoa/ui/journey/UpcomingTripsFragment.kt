package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.databinding.FragmentUpcomingTripsBinding
import com.example.apptauhoa.ui.ticket.BookedTicketAdapter
import com.example.apptauhoa.ui.ticket.TicketRepository

class UpcomingTripsFragment : Fragment() {

    private var _binding: FragmentUpcomingTripsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookedTickets = TicketRepository.bookedTickets
        val adapter = BookedTicketAdapter(bookedTickets) { ticket ->
            val action = JourneyFragmentDirections.actionJourneyToTicketDetails(ticket.tripId, null)
            findNavController().navigate(action)
        }

        binding.recyclerViewUpcomingTrips.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUpcomingTrips.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}