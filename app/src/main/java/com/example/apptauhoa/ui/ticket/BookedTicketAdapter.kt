package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.databinding.ItemBookedTicketBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookedTicketAdapter(
    private val tickets: List<BookedTicket>,
    private val onClick: (BookedTicket) -> Unit
) : RecyclerView.Adapter<BookedTicketAdapter.BookedTicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedTicketViewHolder {
        val binding = ItemBookedTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookedTicketViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: BookedTicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount() = tickets.size

    class BookedTicketViewHolder(
        private val binding: ItemBookedTicketBinding,
        private val onClick: (BookedTicket) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentTicket: BookedTicket? = null

        init {
            itemView.setOnClickListener {
                currentTicket?.let { ticket ->
                    onClick(ticket)
                }
            }
        }

        fun bind(ticket: BookedTicket) {
            currentTicket = ticket
            binding.textDepartureDate.text = formatDate(ticket.departureTime)
            binding.textBookingCode.text = ticket.bookingCode
            binding.textDepartureTime.text = formatTime(ticket.departureTime)
            binding.textArrivalTime.text = formatTime(ticket.arrivalTime)

            val durationMillis = ticket.arrivalTime - ticket.departureTime
            binding.textDuration.text = formatDuration(durationMillis)

            binding.textRouteInfo.text = "${ticket.trainCode} | ${ticket.originStation} - ${ticket.destinationStation}"
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