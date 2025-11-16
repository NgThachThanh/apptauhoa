package com.example.apptauhoa.ui.journey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

private const val VIEW_TYPE_SEAT = 1
private const val VIEW_TYPE_AISLE = 2

class SeatAdapter(
    private val onSeatClicked: (String) -> Unit
) : ListAdapter<Seat, RecyclerView.ViewHolder>(SeatDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSelectable) VIEW_TYPE_SEAT else VIEW_TYPE_AISLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SEAT) {
            val view = inflater.inflate(R.layout.item_seat, parent, false)
            SeatViewHolder(view, onSeatClicked)
        } else {
            val view = inflater.inflate(R.layout.item_seat_aisle, parent, false)
            AisleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SeatViewHolder) {
            holder.bind(getItem(position))
        }
    }

    class SeatViewHolder(
        itemView: View,
        private val onSeatClicked: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val seatNumberText: TextView = itemView.findViewById(R.id.txt_seat_number)

        fun bind(seat: Seat) {
            seatNumberText.text = seat.seatNumber
            
            // The selector drawable handles the background color based on these states.
            itemView.isSelected = seat.status == SeatStatus.SELECTED
            itemView.isEnabled = seat.status != SeatStatus.SOLD

            itemView.setOnClickListener {
                if (itemView.isEnabled) {
                    onSeatClicked(seat.seatId)
                }
            }
        }
    }
    
    class AisleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

class SeatDiffCallback : DiffUtil.ItemCallback<Seat>() {
    override fun areItemsTheSame(oldItem: Seat, newItem: Seat): Boolean {
        return oldItem.seatId == newItem.seatId
    }

    override fun areContentsTheSame(oldItem: Seat, newItem: Seat): Boolean {
        return oldItem == newItem
    }
}