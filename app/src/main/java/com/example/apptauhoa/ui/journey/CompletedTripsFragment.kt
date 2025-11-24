package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.databinding.FragmentCompletedTripsBinding
import com.example.apptauhoa.ui.ticket.BookedTicketAdapter
import com.example.apptauhoa.ui.ticket.TicketRepository

class CompletedTripsFragment : Fragment() {

    private var _binding: FragmentCompletedTripsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assume we have a method in TicketRepository to get completed tickets
        val completedTickets = TicketRepository.bookedTickets.filter { it.status == "completed" }
        val adapter = BookedTicketAdapter(completedTickets) { ticket ->
            val action = JourneyFragmentDirections.actionJourneyToTicketDetails(ticket.bookingCode)
            findNavController().navigate(action)
        }

        binding.recyclerViewCompletedTrips.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCompletedTrips.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
