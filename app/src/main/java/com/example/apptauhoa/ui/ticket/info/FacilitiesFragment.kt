package com.example.apptauhoa.ui.ticket.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FacilitiesFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(requireContext()).apply {
            text = "Thông tin về tiện ích trên tàu...\n- Wifi\n- Canteen\n- Nhà vệ sinh"
            setPadding(48, 48, 48, 48)
        }
        return textView
    }
}