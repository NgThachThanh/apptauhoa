package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.databinding.FragmentPastTripsBinding

class PastTripsFragment : Fragment(R.layout.fragment_past_trips) {

    private var _binding: FragmentPastTripsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPastTripsBinding.bind(view)
        dbHelper = DatabaseHelper(requireContext())

        // Get user ID from shared preferences (assuming it's stored after login)
        val sharedPref = requireActivity().getSharedPreferences("UserSession", 0)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            val completedTickets = dbHelper.getTicketsForUser(userId, "COMPLETED")
            val adapter = PastTripsAdapter(completedTickets) { ticket ->
                val action = JourneyFragmentDirections.actionJourneyToTicketDetails(ticket.tripId, null)
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