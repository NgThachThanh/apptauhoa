package com.example.apptauhoa.ui.ticket

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.data.model.Trip
import com.example.apptauhoa.data.model.User
import com.example.apptauhoa.databinding.FragmentTicketDetailsBinding
import java.text.NumberFormat
import java.util.Locale

class TicketDetailsFragment : Fragment(R.layout.fragment_ticket_details) {

    private var _binding: FragmentTicketDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TicketDetailsFragmentArgs by navArgs()
    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTicketDetailsBinding.bind(view)
        dbHelper = DatabaseHelper(requireContext())

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        loadTicketDetails()
    }

    private fun loadTicketDetails() {
        val tripId = args.tripId ?: return

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId == -1) return

        // This is a simplification. In a real app, you'd have a more robust way
        // to get the specific ticket for this tripId and user.
        val tickets = dbHelper.getTicketsForUser(userId, "BOOKED") + 
                      dbHelper.getTicketsForUser(userId, "COMPLETED") + 
                      dbHelper.getTicketsForUser(userId, "CANCELLED")
        
        val ticket = tickets.firstOrNull { it.tripId == tripId }
        val user = dbHelper.getUserById(userId)

        if (ticket != null && user != null) {
            bindTicketData(ticket, user)
        } 
    }

    private fun bindTicketData(ticket: BookedTicket, user: User) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

        binding.originStationCode.text = ticket.originStation
        binding.destinationStationCode.text = ticket.destinationStation
        // You might need a helper to get full station names from codes if they aren't in the ticket object
        binding.originStationName.text = dbHelper.getAllStations().firstOrNull { it.code == ticket.originStation }?.name ?: ticket.originStation
        binding.destinationStationName.text = dbHelper.getAllStations().firstOrNull { it.code == ticket.destinationStation }?.name ?: ticket.destinationStation
        binding.trainCode.text = ticket.trainCode
        
        binding.passengerName.text = user.fullName
        binding.ticketType.text = "Vé người lớn"
        binding.seatInfo.text = ticket.selectedSeatsInfo
        binding.ticketPrice.text = currencyFormat.format(ticket.originalPrice)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}