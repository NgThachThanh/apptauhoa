package com.example.apptauhoa.ui.ticket.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PolicyFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(requireContext()).apply {
            text = "Chính sách vé và hành lý...\n- Chính sách hoàn hủy\n- Quy định hành lý"
            setPadding(48, 48, 48, 48)
        }
        return textView
    }
}