package com.example.apptauhoa.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.databinding.ItemTrainAdminViewBinding

// A simplified Train class for demonstration
data class Train(val code: String, val departureStation: String, val arrivalStation: String, val departureTime: String, val arrivalTime: String)

class AdminTrainAdapter : ListAdapter<Train, AdminTrainAdapter.TrainViewHolder>(TrainDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TrainViewHolder {
        val binding = ItemTrainAdminViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TrainViewHolder(private val binding: ItemTrainAdminViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(train: Train) {
            binding.textViewTrainCode.text = train.code
            binding.textViewDepartureStation.text = train.departureStation
            binding.textViewArrivalStation.text = train.arrivalStation
            binding.textViewDepartureTime.text = train.departureTime
            binding.textViewArrivalTime.text = train.arrivalTime
        }
    }

    class TrainDiffCallback : DiffUtil.ItemCallback<Train>() {
        override fun areItemsTheSame(oldItem: Train, newItem: Train): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Train, newItem: Train): Boolean {
            return oldItem == newItem
        }
    }
}
