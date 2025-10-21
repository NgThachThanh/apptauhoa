package com.example.apptauhoa.ui.ticket.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewsFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(requireContext()).apply {
            text = "Đánh giá từ hành khách khác..."
            setPadding(48, 48, 48, 48)
        }
        return textView
    }
}