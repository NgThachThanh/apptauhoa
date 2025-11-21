package com.example.apptauhoa.ui.account

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Toolbar Navigation (Back/Close button)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)

        // --- View Binding ---
        val switchNotifications = view.findViewById<SwitchMaterial>(R.id.switchNotifications)
        val tvLanguage = view.findViewById<TextView>(R.id.tvLanguage)
        val switchTheme = view.findViewById<SwitchMaterial>(R.id.switchTheme)
        val tvAbout = view.findViewById<TextView>(R.id.tvAbout)

        // --- Theme Switch Logic ---
        // Set initial state of the switch based on saved preference
        switchTheme.isChecked = sharedPreferences.getBoolean("isDarkMode", false)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Turn on Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("isDarkMode", true).apply()
            } else {
                // Turn off Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("isDarkMode", false).apply()
            }
        }

        // --- Other Settings Logic (existing code) ---
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Đã bật thông báo" else "Đã tắt thông báo"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        tvLanguage.setOnClickListener {
            Toast.makeText(requireContext(), "Mở cài đặt ngôn ngữ (demo)", Toast.LENGTH_SHORT).show()
        }

        tvAbout.setOnClickListener {
            Toast.makeText(requireContext(), "Mở màn hình Về ứng dụng (demo)", Toast.LENGTH_SHORT).show()
        }
    }
}