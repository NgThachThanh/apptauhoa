package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R

class RouteDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_route_detail, container, false)

        val routeTitleTextView: TextView = view.findViewById(R.id.route_title_text_view)
        val distanceTextView: TextView = view.findViewById(R.id.distance_text_view)
        val durationTextView: TextView = view.findViewById(R.id.duration_text_view)
        val priceTextView: TextView = view.findViewById(R.id.price_text_view)

        // Dummy data for now
        routeTitleTextView.text = "Hà Nội → Hải Phòng"
        distanceTextView.text = "Quãng đường: 102km"
        durationTextView.text = "Thời gian: 2h00"
        priceTextView.text = "Giá từ: 120.000đ"

        return view
    }
}