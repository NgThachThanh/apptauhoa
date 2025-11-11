package com.example.apptauhoa.ui.account // Sửa lại package cho đúng

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchNotifications = view.findViewById<SwitchMaterial>(R.id.switchNotifications)
        val tvLanguage = view.findViewById<TextView>(R.id.tvLanguage)
        val tvAbout = view.findViewById<TextView>(R.id.tvAbout)

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Đã bật thông báo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Đã tắt thông báo", Toast.LENGTH_SHORT).show()
            }
        }

        tvLanguage.setOnClickListener {
            Toast.makeText(requireContext(), "Mở cài đặt ngôn ngữ (demo)", Toast.LENGTH_SHORT).show()
        }

        tvAbout.setOnClickListener {
            Toast.makeText(requireContext(), "Mở màn hình Về ứng dụng (demo)", Toast.LENGTH_SHORT).show()
        }
    }
}