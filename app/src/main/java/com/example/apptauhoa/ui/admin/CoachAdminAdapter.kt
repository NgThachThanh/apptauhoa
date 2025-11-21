package com.example.apptauhoa.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.data.model.Coach
import com.example.apptauhoa.databinding.ItemCoachAdminBinding
import java.text.NumberFormat
import java.util.Locale

class CoachAdminAdapter(
    private val coaches: MutableList<Coach>,
    private val onDeleteClick: (Coach) -> Unit
) : RecyclerView.Adapter<CoachAdminAdapter.CoachViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachViewHolder {
        val binding = ItemCoachAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoachViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoachViewHolder, position: Int) {
        holder.bind(coaches[position])
    }

    override fun getItemCount() = coaches.size

    fun addCoach(coach: Coach) {
        coaches.add(coach)
        notifyItemInserted(coaches.size - 1)
    }

    fun removeCoach(coach: Coach) {
        val position = coaches.indexOf(coach)
        if (position != -1) {
            coaches.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    
    fun getCoaches(): List<Coach> = coaches

    inner class CoachViewHolder(private val binding: ItemCoachAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coach: Coach) {
            binding.tvCoachName.text = coach.name
            binding.tvCoachType.text = "${coach.type} • ${coach.totalSeats} ghế"
            
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            binding.tvCoachPrice.text = currencyFormat.format(coach.price)

            binding.btnDeleteCoach.setOnClickListener {
                onDeleteClick(coach)
            }
        }
    }
}