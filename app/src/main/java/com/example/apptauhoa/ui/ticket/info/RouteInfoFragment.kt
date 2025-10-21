package com.example.apptauhoa.ui.ticket.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RouteInfoFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(requireContext()).apply {
            text = "Thông tin chi tiết về lộ trình...\n- Ga đi\n- Ga đến\n- Thời gian dự kiến"
            setPadding(48, 48, 48, 48)
        }
        return textView
    }
}