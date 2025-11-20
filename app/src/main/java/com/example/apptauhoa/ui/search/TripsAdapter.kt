package com.example.apptauhoa.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.google.android.material.card.MaterialCardView

class TripsAdapter(
    private var trips: List<Trip>,
    private val onItemClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position], onItemClick)
    }

    override fun getItemCount() = trips.size

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(trip: Trip, onItemClick: (Trip) -> Unit) {
            // Fix for the layout change in item_trip.xml
            // Map Trip data to the new views.

            // R.id.txt_class_title is removed. Using txt_booking_date for train code.
            itemView.findViewById<TextView>(R.id.txt_booking_date).text = trip.trainCode

            // Using txt_status for the price in search results.
            itemView.findViewById<TextView>(R.id.txt_status).text = trip.price

            itemView.findViewById<TextView>(R.id.txt_departure_time).text = trip.departureTime
            itemView.findViewById<TextView>(R.id.txt_arrival_time).text = trip.arrivalTime
            itemView.findViewById<TextView>(R.id.txt_duration).text = trip.duration

            // Combine origin and destination into one TextView
            itemView.findViewById<TextView>(R.id.txt_origin_station_line1).text = "${trip.originStation} - ${trip.destinationStation}"

            itemView.findViewById<MaterialCardView>(R.id.card_trip).setOnClickListener {
                onItemClick(trip)
            }
        }
    }
}