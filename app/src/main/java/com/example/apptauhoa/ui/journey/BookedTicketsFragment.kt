package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.databinding.FragmentBookedTicketsBinding
import com.example.apptauhoa.ui.ticket.BookedTicketAdapter
import com.example.apptauhoa.ui.ticket.TicketRepository

class BookedTicketsFragment : Fragment() {

    private var _binding: FragmentBookedTicketsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookedTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookedTickets = TicketRepository.bookedTickets
        val adapter = BookedTicketAdapter(bookedTickets) { ticket ->
            val action = JourneyFragmentDirections.actionJourneyToTicketDetails(ticket.bookingCode)
            findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}