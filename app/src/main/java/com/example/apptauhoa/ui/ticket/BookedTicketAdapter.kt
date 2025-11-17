package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.databinding.ItemBookedTicketBinding
import java.text.NumberFormat
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
            val tripParts = ticket.tripSummary.split(" → ")
            if (tripParts.size == 2) {
                binding.textDepartureStation.text = tripParts[0]
                binding.textArrivalStation.text = tripParts[1]
            } else {
                // Handle cases where the summary is not in the expected format
                binding.textDepartureStation.text = ticket.tripSummary
                binding.textArrivalStation.text = ""
            }

            binding.textTrainName.text = ticket.tripId // Assuming tripId is the train name
            binding.textBookingCode.text = ticket.bookingCode

            binding.textDepartureTime.text = formatTime(ticket.departureTime)
            binding.textArrivalTime.text = formatTime(ticket.arrivalTime)

            val durationMillis = ticket.arrivalTime - ticket.departureTime
            binding.textDuration.text = formatDuration(durationMillis)

            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            binding.textPrice.text = "Từ ${currencyFormat.format(ticket.originalPrice)}"
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