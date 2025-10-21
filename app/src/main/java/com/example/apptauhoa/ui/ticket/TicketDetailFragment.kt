package com.example.apptauhoa.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R

class TicketDetailFragment : Fragment() {

    private var tripId: String? = null
    private var selectionResult: SeatSelectionResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getString("tripId")
            selectionResult = it.getParcelable("selectionResult")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_detail, container, false)
        val tripIdTextView = view.findViewById<TextView>(R.id.trip_id_text)
        
        if (selectionResult != null) {
            tripIdTextView.text = "Selected ${selectionResult!!.selectedSeats.size} seats for a total of ${selectionResult!!.totalPrice}đ"
        } else {
            tripIdTextView.text = "Trip ID: $tripId"
        }
        
        return view
    }
}