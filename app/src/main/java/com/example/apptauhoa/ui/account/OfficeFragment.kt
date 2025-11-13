package com.example.apptauhoa.ui.account // Sửa lại package cho đúng

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R

/**
 * Fragment hiển thị danh sách văn phòng.
 * Chỉ hiển thị layout, không cần logic phức tạp.
 */
class OfficeFragment : Fragment(R.layout.fragment_office) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Không cần logic gì thêm cho màn hình tĩnh này
    }
}