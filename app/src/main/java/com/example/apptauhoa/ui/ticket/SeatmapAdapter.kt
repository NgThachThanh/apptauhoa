package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class SeatmapAdapter(
    private var seats: MutableList<Seat>, // Now requires a mutable list
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
        seats[position] = seat
        notifyItemChanged(position)
    }
    
    fun updateSeats(newSeats: List<Seat>) {
        seats = newSeats.toMutableList()
        notifyDataSetChanged()
    }

    class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val seatNumber: TextView = itemView.findViewById(R.id.txt_seat_number)

        fun bind(seat: Seat, position: Int, onSeatClick: (Seat, Int) -> Unit) {
            seatNumber.text = seat.number
            
            val backgroundRes = when (seat.status) {
                SeatStatus.AVAILABLE -> R.drawable.bg_seat_available
                SeatStatus.SOLD -> R.drawable.bg_seat_sold
                SeatStatus.SELECTED -> R.drawable.bg_seat_selected
                SeatStatus.BLOCKED -> R.drawable.bg_seat_sold
            }
            itemView.setBackgroundResource(backgroundRes)
            
            seatNumber.visibility = if(seat.number.isEmpty()) View.INVISIBLE else View.VISIBLE

            if (seat.status == SeatStatus.AVAILABLE || seat.status == SeatStatus.SELECTED) {
                itemView.setOnClickListener { onSeatClick(seat, position) }
            } else {
                itemView.setOnClickListener(null)
            }
        }
    }
}