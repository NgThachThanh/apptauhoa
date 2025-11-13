package com.example.apptauhoa.ui.journey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import java.text.NumberFormat
import java.util.Locale

class TrainScheduleAdapter(
    private var trips: List<TrainTrip>,
    private val onItemClicked: (TrainTrip) -> Unit
) : RecyclerView.Adapter<TrainScheduleAdapter.TrainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_train_schedule, parent, false)
        return TrainViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val trip = trips[position]
        holder.bind(trip)
        holder.itemView.setOnClickListener { onItemClicked(trip) }
    }

    override fun getItemCount(): Int = trips.size
    
    fun updateData(newTrips: List<TrainTrip>) {
        trips = newTrips
        notifyDataSetChanged()
    }

    class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coachClass: TextView = itemView.findViewById(R.id.txt_coach_class)
        private val seatsLeft: TextView = itemView.findViewById(R.id.txt_seats_left)
        private val departureTime: TextView = itemView.findViewById(R.id.txt_departure_time)
        private val arrivalTime: TextView = itemView.findViewById(R.id.txt_arrival_time)
        private val duration: TextView = itemView.findViewById(R.id.txt_duration)
        private val originStation: TextView = itemView.findViewById(R.id.txt_origin_station)
        private val destinationStation: TextView = itemView.findViewById(R.id.txt_destination_station)
        private val trainCode: TextView = itemView.findViewById(R.id.txt_train_code)
        private val ticketPrice: TextView = itemView.findViewById(R.id.txt_ticket_price)

        fun bind(trip: TrainTrip) {
            coachClass.text = trip.coachClass
            seatsLeft.text = "Còn ${trip.availableSeats} chỗ"
            departureTime.text = trip.departureTime
            arrivalTime.text = trip.arrivalTime
            duration.text = trip.duration
            originStation.text = trip.originStation
            destinationStation.text = trip.destinationStation
            trainCode.text = trip.trainCode
            
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            ticketPrice.text = "Từ ${currencyFormat.format(trip.price)}"
        }
    }
}