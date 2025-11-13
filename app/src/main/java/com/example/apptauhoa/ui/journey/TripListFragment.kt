package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R

class TripListFragment : Fragment() {

    private var tripType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripType = it.getInt(ARG_TRIP_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Just using a simple TextView for demonstration
        val view = inflater.inflate(R.layout.fragment_trip_list, container, false)
        val textView = view.findViewById<TextView>(R.id.placeholder_text) // Assuming you have a TextView with this ID
        
        textView.text = when (tripType) {
            0 -> "Hiển thị danh sách các chuyến đi SẮP ĐI"
            1 -> "Hiển thị danh sách các chuyến đi ĐÃ ĐI"
            2 -> "Hiển thị danh sách các chuyến đi ĐÃ HỦY"
            else -> "Danh sách trống"
        }
        
        return view
    }

    companion object {
        private const val ARG_TRIP_TYPE = "trip_type"

        fun newInstance(tripType: Int): TripListFragment {
            val fragment = TripListFragment()
            val args = Bundle()
            args.putInt(ARG_TRIP_TYPE, tripType)
            fragment.arguments = args
            return fragment
        }
    }
}