package com.example.apptauhoa.ui.account // Sửa lại package cho đúng với project của bạn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R
import androidx.navigation.fragment.findNavController

/**
 * Fragment mới để hiển thị thông tin "Giới thiệu nhà tàu".
 * Nó sử dụng layout R.layout.fragment_intro
 */
class IntroFragment : Fragment(R.layout.fragment_intro) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hiện tại, fragment này chỉ hiển thị layout.
        // Nếu bạn thêm một nút "Quay lại" tùy chỉnh vào file XML
        // (ví dụ: với id là btnBack), bạn có thể thêm sự kiện click ở đây:
        //
        // val btnBack = view.findViewById<View>(R.id.btnBack)
        // btnBack.setOnClickListener {
        //     findNavController().popBackStack() // Quay lại màn hình trước đó
        // }

        // Tuy nhiên, thông thường thanh AppBar của Activity
        // sẽ tự động xử lý nút "Quay lại" khi bạn dùng Navigation Component.
    }
}