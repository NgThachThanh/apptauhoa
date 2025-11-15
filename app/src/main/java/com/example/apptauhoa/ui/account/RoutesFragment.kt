package com.example.apptauhoa.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R
import com.google.android.material.card.MaterialCardView

class RoutesFragment : Fragment(R.layout.fragment_routes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Bind Views ---
        val routeHcmHanoiCard = view.findViewById<MaterialCardView>(R.id.route_hcm_hanoi_card)
        val routeHcmDanangCard = view.findViewById<MaterialCardView>(R.id.route_hcm_danang_card)
        val routeHanoiLaoCaiCard = view.findViewById<MaterialCardView>(R.id.route_hanoi_laocai_card)
        val routeDanangNhaTrangCard = view.findViewById<MaterialCardView>(R.id.route_danang_nhatrang_card)
        val routeHanoiHaiphongCard = view.findViewById<MaterialCardView>(R.id.route_hanoi_haiphong_card)

        // --- Set Click Listeners ---

        routeHcmHanoiCard.setOnClickListener {
            showToast("Chi tiết tuyến Hồ Chí Minh - Hà Nội")
        }

        routeHcmDanangCard.setOnClickListener {
            showToast("Chi tiết tuyến Hồ Chí Minh - Đà Nẵng")
        }

        routeHanoiLaoCaiCard.setOnClickListener {
            showToast("Chi tiết tuyến Hà Nội - Lào Cai (Sapa)")
        }

        routeDanangNhaTrangCard.setOnClickListener {
            showToast("Chi tiết tuyến Đà Nẵng - Nha Trang")
        }

        routeHanoiHaiphongCard.setOnClickListener {
            showToast("Chi tiết tuyến Hà Nội - Hải Phòng")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}