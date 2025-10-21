package com.example.apptauhoa.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class SearchResultsAdapter(private val results: List<SearchResult>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resultTextView: TextView = view.findViewById(R.id.result_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.resultTextView.text = "${result.trainName} | ${result.departureStation} ${result.departureTime} → ${result.arrivalStation} ${result.arrivalTime} | ${result.duration} | ${result.price}"
    }

    override fun getItemCount() = results.size
}