package com.example.apptauhoa.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.data.model.Promotion
import com.example.apptauhoa.databinding.ItemPromotionBinding

class PromotionAdapter(
    private val onClick: (Promotion) -> Unit
) : ListAdapter<Promotion, PromotionAdapter.PromotionViewHolder>(PromotionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val binding = ItemPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromotionViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        val promotion = getItem(position)
        holder.bind(promotion)
    }

    class PromotionViewHolder(
        private val binding: ItemPromotionBinding,
        private val onClick: (Promotion) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentPromotion: Promotion? = null

        init {
            itemView.setOnClickListener {
                currentPromotion?.let {
                    onClick(it)
                }
            }
        }

        fun bind(promotion: Promotion) {
            currentPromotion = promotion
            binding.textViewPromotionTitle.text = promotion.title
            // In a real app, you would load the image from promotion.imageUrl using Glide, Picasso, or Coil.
            // For this example, we'll just set a placeholder.
            // e.g. binding.imageViewPromotionBanner.load(promotion.imageUrl)
        }
    }
}

object PromotionDiffCallback : DiffUtil.ItemCallback<Promotion>() {
    override fun areItemsTheSame(oldItem: Promotion, newItem: Promotion): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Promotion, newItem: Promotion): Boolean {
        return oldItem == newItem
    }
}
