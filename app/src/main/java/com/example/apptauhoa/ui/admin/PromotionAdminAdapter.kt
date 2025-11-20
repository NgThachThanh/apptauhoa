package com.example.apptauhoa.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.databinding.ItemPromotionAdminBinding

// A simplified Promotion class for demonstration
data class Promotion(val id: String, val title: String, val code: String, val discount: String, val imageUrl: String)

class PromotionAdminAdapter(
    private val onEditClick: (Promotion) -> Unit,
    private val onDeleteClick: (Promotion) -> Unit
) : ListAdapter<Promotion, PromotionAdminAdapter.PromotionViewHolder>(PromotionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            PromotionViewHolder {
        val binding = ItemPromotionAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromotionViewHolder(binding, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PromotionViewHolder(
        private val binding: ItemPromotionAdminBinding,
        private val onEditClick: (Promotion) -> Unit,
        private val onDeleteClick: (Promotion) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(promotion: Promotion) {
            binding.textViewPromotionTitle.text = promotion.title
            binding.textViewPromotionDetails.text = "Mã: ${promotion.code} - Giảm ${promotion.discount}"
            // In a real app, you would load the image from promotion.imageUrl using a library like Glide or Coil

            binding.buttonEdit.setOnClickListener { onEditClick(promotion) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(promotion) }
        }
    }

    class PromotionDiffCallback : DiffUtil.ItemCallback<Promotion>() {
        override fun areItemsTheSame(oldItem: Promotion, newItem: Promotion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Promotion, newItem: Promotion): Boolean {
            return oldItem == newItem
        }
    }
}
