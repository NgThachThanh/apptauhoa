package com.example.apptauhoa.ui.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.databinding.ItemBedBinding

class BedAdapter(private val beds: List<Seat>, private val onBedSelected: (Seat) -> Unit) :
    RecyclerView.Adapter<BedAdapter.BedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BedViewHolder {
        val binding = ItemBedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BedViewHolder, position: Int) {
        holder.bind(beds[position], onBedSelected)
    }

    override fun getItemCount() = beds.size

    class BedViewHolder(private val binding: ItemBedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bed: Seat, onBedSelected: (Seat) -> Unit) {
            binding.textBedNumber.text = bed.number
            binding.root.setOnClickListener { onBedSelected(bed) }

            val context = binding.root.context
            when (bed.status) {
                SeatStatus.AVAILABLE -> {
                    binding.root.setCardBackgroundColor(context.getColor(android.R.color.white))
                    binding.textBedNumber.setTextColor(context.getColor(android.R.color.black))
                }
                SeatStatus.SELECTED -> {
                    binding.root.setCardBackgroundColor(context.getColor(com.google.android.material.R.color.design_default_color_primary))
                    binding.textBedNumber.setTextColor(context.getColor(android.R.color.white))
                }
                SeatStatus.BOOKED -> {
                    binding.root.setCardBackgroundColor(context.getColor(android.R.color.darker_gray))
                    binding.textBedNumber.setTextColor(context.getColor(android.R.color.white))
                }
                SeatStatus.PENDING -> {
                    binding.root.setCardBackgroundColor(context.getColor(android.R.color.holo_orange_light))
                    binding.textBedNumber.setTextColor(context.getColor(android.R.color.white))
                }
            }
        }
    }
}