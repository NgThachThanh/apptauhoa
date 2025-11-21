package com.example.apptauhoa.ui.journey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.example.apptauhoa.data.model.Coach
import java.text.NumberFormat
import java.util.Locale

class CoachAdapter(
    private var coaches: List<Coach>,
    private val onItemClicked: (Coach) -> Unit
) : RecyclerView.Adapter<CoachAdapter.CoachViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coach, parent, false)
        return CoachViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoachViewHolder, position: Int) {
        val coach = coaches[position]
        holder.bind(coach)
        holder.itemView.setOnClickListener { onItemClicked(coach) }
    }

    override fun getItemCount(): Int = coaches.size

    class CoachViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.txt_coach_name)
        private val type: TextView = itemView.findViewById(R.id.txt_coach_type)
        private val available: TextView = itemView.findViewById(R.id.txt_available)
        private val price: TextView = itemView.findViewById(R.id.txt_price)

        fun bind(coach: Coach) {
            name.text = coach.name
            type.text = coach.type
            available.text = "Còn ${coach.availableSeats}/${coach.totalSeats} chỗ"
            
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            price.text = currencyFormat.format(coach.price)
        }
    }
}