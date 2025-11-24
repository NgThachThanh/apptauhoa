package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.databinding.FragmentUpcomingTripsBinding

class UpcomingTripsFragment : Fragment(R.layout.fragment_upcoming_trips) {

    private var _binding: FragmentUpcomingTripsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingTripsBinding.bind(view)
        dbHelper = DatabaseHelper(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("UserSession", 0)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            val upcomingTickets = dbHelper.getTicketsForUser(userId, "BOOKED")
            val adapter = PastTripsAdapter(upcomingTickets) { ticket ->
                val action = JourneyFragmentDirections.actionJourneyToTicketDetails(ticket.bookingCode)
                findNavController().navigate(action)
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}