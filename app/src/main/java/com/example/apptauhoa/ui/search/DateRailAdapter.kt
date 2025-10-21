package com.example.apptauhoa.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateRailAdapter(
    private val dates: List<LocalDate>
) : RecyclerView.Adapter<DateRailAdapter.DateViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_chip, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount() = dates.size
    
    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(date: LocalDate) {
            val formatter = DateTimeFormatter.ofPattern("dd\nMMM", Locale("vi", "VN"))
            itemView.findViewById<TextView>(R.id.date_text).text = date.format(formatter)
        }
    }
}