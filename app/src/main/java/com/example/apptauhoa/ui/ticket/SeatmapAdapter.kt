package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class SeatmapAdapter(
    var seats: MutableList<Seat>,
    private val onSeatClick: (Seat, Int) -> Unit
) : RecyclerView.Adapter<SeatmapAdapter.SeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        holder.bind(seats[position], position, onSeatClick)
    }

    override fun getItemCount() = seats.size

    fun updateSeat(position: Int, seat: Seat) {
        if (position >= 0 && position < seats.size) {
            seats[position] = seat
            notifyItemChanged(position)
        }
    }

    fun updateSeats(newSeats: List<Seat>) {
        seats.clear()
        seats.addAll(newSeats)
        notifyDataSetChanged()
    }

    class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val seatNumber: TextView = itemView.findViewById(R.id.seat_number)

        fun bind(seat: Seat, position: Int, onSeatClick: (Seat, Int) -> Unit) {
            seatNumber.text = seat.number

            // Set background based on seat status
            val backgroundRes = when (seat.status) {
                SeatStatus.AVAILABLE -> R.drawable.bg_seat_available
                SeatStatus.BOOKED -> R.drawable.bg_seat_sold // Using bg_seat_sold for booked seats
                SeatStatus.SELECTED -> R.drawable.bg_seat_selected
            }
            seatNumber.setBackgroundResource(backgroundRes)

            // FINAL FIX:
            // The previous code made seats with empty numbers invisible.
            // This was the likely cause of the blank screen if seat data had empty 'number' fields.
            // By removing this visibility check, all seat items will now be rendered.

            // Instead of making seats invisible, we just control their clickability.
            if (seat.number.isEmpty() || seat.status == SeatStatus.BOOKED) {
                itemView.isClickable = false
                itemView.setOnClickListener(null)
            } else {
                itemView.isClickable = true
                itemView.setOnClickListener { onSeatClick(seat, position) }
            }
        }
    }
}