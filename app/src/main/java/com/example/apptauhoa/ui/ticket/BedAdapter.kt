package com.example.apptauhoa.ui.ticket

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.data.model.Seat
import com.example.apptauhoa.data.model.SeatStatus
import com.example.apptauhoa.databinding.ItemBedBinding

class BedAdapter(private val onBedClicked: (Seat) -> Unit) : ListAdapter<Seat, BedAdapter.BedViewHolder>(BedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BedViewHolder {
        val binding = ItemBedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BedViewHolder(binding, onBedClicked)
    }

    override fun onBindViewHolder(holder: BedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BedViewHolder(
        private val binding: ItemBedBinding,
        private val onBedClicked: (Seat) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(seat: Seat) {
            binding.bedButton.text = seat.number
            binding.root.setOnClickListener { onBedClicked(seat) }

            val (backgroundColor, textColor, strokeWidth) = when (seat.status) {
                SeatStatus.AVAILABLE -> Triple(Color.WHITE, Color.BLACK, 2)
                SeatStatus.SELECTED -> Triple(Color.parseColor("#2196F3"), Color.WHITE, 0)
                SeatStatus.BOOKED -> Triple(Color.DKGRAY, Color.WHITE, 0)
            }

            binding.bedButton.backgroundTintList = ColorStateList.valueOf(backgroundColor)
            binding.bedButton.setTextColor(textColor)
            binding.bedButton.strokeWidth = strokeWidth
            if (strokeWidth > 0) {
                binding.bedButton.strokeColor = ColorStateList.valueOf(Color.LTGRAY)
            }
        }
    }
}

private class BedDiffCallback : DiffUtil.ItemCallback<Seat>() {
    override fun areItemsTheSame(oldItem: Seat, newItem: Seat): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Seat, newItem: Seat): Boolean = oldItem == newItem
}