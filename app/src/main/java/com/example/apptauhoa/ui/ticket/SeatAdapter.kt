package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class SeatAdapter(private val onSeatClick: (Seat) -> Unit) : 
    ListAdapter<Seat, SeatAdapter.SeatViewHolder>(SeatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = getItem(position)
        holder.bind(seat, onSeatClick)
    }

    class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val seatNumber: TextView = itemView.findViewById(R.id.seat_number)

        fun bind(seat: Seat, onSeatClick: (Seat) -> Unit) {
            seatNumber.text = seat.number
            
            val backgroundRes = when (seat.status) {
                SeatStatus.AVAILABLE -> R.drawable.bg_seat_available
                SeatStatus.BOOKED -> R.drawable.bg_seat_booked
                SeatStatus.SELECTED -> R.drawable.bg_seat_selected
            }
            seatNumber.setBackgroundResource(backgroundRes)

            itemView.setOnClickListener {
                if (seat.status != SeatStatus.BOOKED) {
                    onSeatClick(seat)
                }
            }
        }
    }
}

class SeatDiffCallback : DiffUtil.ItemCallback<Seat>() {
    override fun areItemsTheSame(oldItem: Seat, newItem: Seat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Seat, newItem: Seat): Boolean {
        return oldItem == newItem
    }
}