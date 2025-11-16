package com.example.apptauhoa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class PromotionAdapter(
    private val onClick: (Promotion) -> Unit
) : ListAdapter<Promotion, PromotionAdapter.PromotionViewHolder>(PromotionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promotion, parent, false)
        return PromotionViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PromotionViewHolder(
        itemView: View,
        val onClick: (Promotion) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.textView_promotion_title)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView_promotion_banner)
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
            titleView.text = promotion.title
            // In a real app, you would load promotion.imageUrl here using Glide/Coil
            // For now, the placeholder from the layout is sufficient.
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