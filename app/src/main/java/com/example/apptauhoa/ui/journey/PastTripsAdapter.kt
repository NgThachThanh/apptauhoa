package com.example.apptauhoa.ui.journey

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.data.model.BookedTicket
import com.example.apptauhoa.databinding.ItemPastTripBinding

class PastTripsAdapter(
    private val tickets: List<BookedTicket>,
    private val onClick: (BookedTicket) -> Unit
) : RecyclerView.Adapter<PastTripsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPastTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount() = tickets.size

    class ViewHolder(
        private val binding: ItemPastTripBinding, 
        private val onClick: (BookedTicket) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket: BookedTicket) {
            binding.tvTrainCode.text = ticket.trainCode
            binding.tvRoute.text = "${ticket.originStation} - ${ticket.destinationStation}"
            binding.tvDateTime.text = "${ticket.tripDate} | ${ticket.departureTime}"
            binding.tvBookingCode.text = "Mã đặt chỗ: ${ticket.bookingCode}"
            binding.tvSeatInfo.text = "Chỗ ngồi: ${ticket.selectedSeatsInfo}"
            itemView.setOnClickListener { onClick(ticket) }
        }
    }
}