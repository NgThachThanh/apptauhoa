package com.example.apptauhoa.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.google.android.material.chip.Chip
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateRailAdapter(
    private val dates: List<LocalDate>,
    private val onDateSelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<DateRailAdapter.DateViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_chip, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date, position == selectedPosition)

        holder.itemView.setOnClickListener {
            if (selectedPosition != holder.adapterPosition) {
                val oldPosition = selectedPosition
                selectedPosition = holder.adapterPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onDateSelected(dates[selectedPosition])
            }
        }
    }

    override fun getItemCount() = dates.size

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // The itemView is the Chip itself.
        private val chip: Chip = itemView as Chip

        fun bind(date: LocalDate, isSelected: Boolean) {
            // Format the date into a single string like "T3, 12/11"
            val formatter = DateTimeFormatter.ofPattern("E, dd/MM", Locale("vi", "VN"))
            chip.text = date.format(formatter).replaceFirstChar { it.uppercase() }
            chip.isChecked = isSelected
        }
    }
}