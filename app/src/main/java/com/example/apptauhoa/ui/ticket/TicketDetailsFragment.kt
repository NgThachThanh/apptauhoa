package com.example.apptauhoa.ui.ticket

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.data.model.User
import com.example.apptauhoa.databinding.FragmentTicketDetailsBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
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
        val bookingCode = args.ticketBookingCode
        if (bookingCode.isEmpty()) return

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)
        if (userId == -1) return

        val ticket = dbHelper.getTicketByBookingCode(bookingCode)
        val user = dbHelper.getUserById(userId)

        if (ticket != null && user != null) {
            bindTicketData(ticket, user)
        }
    }

    private fun bindTicketData(ticket: BookedTicket, user: User) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val timeFormat = SimpleDateFormat("HH:mm, dd/MM", Locale.getDefault())

        binding.originStationCode.text = ticket.originStation
        binding.destinationStationCode.text = ticket.destinationStation
        binding.originStationName.text = dbHelper.getAllStations().firstOrNull { it.code == ticket.originStation }?.name ?: ticket.originStation
        binding.destinationStationName.text = dbHelper.getAllStations().firstOrNull { it.code == ticket.destinationStation }?.name ?: ticket.destinationStation
        binding.trainCode.text = ticket.trainCode
        
        binding.passengerName.text = user.fullName
        binding.ticketType.text = "Vé người lớn"
        binding.seatInfo.text = ticket.selectedSeatsInfo
        binding.ticketPrice.text = currencyFormat.format(ticket.originalPrice)

        binding.departureTime.text = timeFormat.format(ticket.departureTime)
        binding.arrivalTime.text = timeFormat.format(ticket.arrivalTime)

        binding.buttonCancelTicket.setOnClickListener {
            showCancelConfirmationDialog(ticket.bookingCode)
        }
    }

    private fun showCancelConfirmationDialog(bookingCode: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận hủy vé")
            .setMessage("Bạn có chắc chắn muốn hủy vé này không? Hành động này không thể hoàn tác.")
            .setPositiveButton("Xác nhận") { _, _ ->
                cancelTicket(bookingCode)
            }
            .setNegativeButton("Không", null)
            .show()
    }

    private fun cancelTicket(bookingCode: String) {
        val updatedRows = dbHelper.updateTicketStatus(bookingCode, "CANCELLED")
        if (updatedRows > 0) {
            findNavController().navigate(R.id.navigation_journey)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}