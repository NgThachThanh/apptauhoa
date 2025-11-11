package com.example.apptauhoa.ui.account // Sửa lại package cho đúng

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R

class RoutesFragment : Fragment(R.layout.fragment_routes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bạn có thể thêm sự kiện click cho từng lộ trình
        val route1 = view.findViewById<LinearLayout>(R.id.route_hanoi_saigon)
        route1.setOnClickListener {
            Toast.makeText(requireContext(), "Xem chi tiết Sài Gòn - Hà Nội", Toast.LENGTH_SHORT).show()
        }

        val route2 = view.findViewById<LinearLayout>(R.id.route_saigon_danang)
        route2.setOnClickListener {
            Toast.makeText(requireContext(), "Xem chi tiết Sài Gòn - Đà Nẵng", Toast.LENGTH_SHORT).show()
        }
    }
}