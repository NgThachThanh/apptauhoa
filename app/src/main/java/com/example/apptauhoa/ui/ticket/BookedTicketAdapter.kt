package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.databinding.ItemBookedTicketBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookedTicketAdapter(private val tickets: List<BookedTicket>) :
    RecyclerView.Adapter<BookedTicketAdapter.BookedTicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedTicketViewHolder {
        val binding = ItemBookedTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookedTicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookedTicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount() = tickets.size

    class BookedTicketViewHolder(private val binding: ItemBookedTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: BookedTicket) {
            // The binding logic has been updated to use the correct view IDs from the new layout.
            binding.textDepartureDate.text = formatDate(ticket.departureTime)

            binding.textBookingCode.text = ticket.bookingCode

            binding.textDepartureTime.text = formatTime(ticket.departureTime)
            binding.textArrivalTime.text = formatTime(ticket.arrivalTime)

            val durationMillis = ticket.arrivalTime - ticket.departureTime
            binding.textDuration.text = formatDuration(durationMillis)

            val route = ticket.tripSummary.replace(" → ", " - ")
            binding.textRouteInfo.text = "${ticket.tripId} | $route"
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

        private fun formatTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

        private fun formatDuration(millis: Long): String {
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
            return "${hours}h ${minutes}m"
        }
    }
}