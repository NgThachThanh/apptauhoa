package com.example.apptauhoa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R

class PopularRoutesAdapter(private val routes: List<PopularRoute>) : RecyclerView.Adapter<PopularRoutesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routeTextView: TextView = view.findViewById(R.id.route_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_popular_route, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = routes[position]
        holder.routeTextView.text = "${route.from} → ${route.to} | ${route.distance} | ${route.duration} | ${route.price}"
    }

    override fun getItemCount() = routes.size
}