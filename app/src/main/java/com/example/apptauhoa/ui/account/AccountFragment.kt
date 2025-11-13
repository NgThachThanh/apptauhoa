package com.example.apptauhoa.ui.account

import android.os.Bundle
import android.util.Log // <-- Import thư viện Log để debug
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R

/**
 * File này điều khiển fragment_account.xml
 * Nó xử lý sự kiện click cho TẤT CẢ 5 mục có thể tương tác.
 */
class AccountFragment : Fragment(R.layout.fragment_account) {

    // Tag để lọc trong cửa sổ Logcat
    private val TAG = "AccountFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: AccountFragment đã được tạo.")

        // --- BƯỚC 1: Ánh xạ 5 view từ file XML ---
        // (Điều này yêu cầu fragment_account.xml phải có 5 ID này)
        val btnLoginNow: Button = view.findViewById(R.id.btnLoginNow)
        val tvIntro: TextView = view.findViewById(R.id.tvIntro)
        val tvRoutes: TextView = view.findViewById(R.id.tvRoutes)
        val tvOffice: TextView = view.findViewById(R.id.tvOffice)
        val tvSettings: TextView = view.findViewById(R.id.tvSettings)

        // --- BƯỚC 2: Gán sự kiện click cho 5 view ---

        // 1. Nút Đăng nhập ngay
        btnLoginNow.setOnClickListener {
            Log.d(TAG, "CLICK: Đã nhấn 'Đăng nhập ngay'")
            try {
                // Sử dụng action ID từ nav_graph.xml
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
            } catch (e: Exception) {
                Log.e(TAG, "LỖI: không thể điều hướng đến loginFragment", e)
            }
        }

        // 2. Option: Giới thiệu nhà tàu
        tvIntro.setOnClickListener {
            Log.d(TAG, "CLICK: Đã nhấn 'Giới thiệu nhà tàu'")
            try {
                // Sử dụng action ID từ nav_graph.xml
                findNavController().navigate(R.id.action_accountFragment_to_introFragment)
            } catch (e: Exception) {
                Log.e(TAG, "LỖI: không thể điều hướng đến introFragment", e)
            }
        }

        // 3. Option: Lộ trình phổ biến
        tvRoutes.setOnClickListener {
            Log.d(TAG, "CLICK: Đã nhấn 'Lộ trình phổ biến'")
            try {
                // Sử dụng action ID từ nav_graph.xml
                findNavController().navigate(R.id.action_accountFragment_to_routesFragment)
            } catch (e: Exception) {
                Log.e(TAG, "LỖI: không thể điều hướng đến routesFragment", e)
            }
        }

        // 4. Option: Văn phòng nhà tàu
        tvOffice.setOnClickListener {
            Log.d(TAG, "CLICK: Đã nhấn 'Văn phòng nhà tàu'")
            try {
                // Sử dụng action ID từ nav_graph.xml
                findNavController().navigate(R.id.action_accountFragment_to_officeFragment)
            } catch (e: Exception) {
                Log.e(TAG, "LỖI: không thể điều hướng đến officeFragment", e)
            }
        }

        // 5. Option: Cài đặt
        tvSettings.setOnClickListener {
            Log.d(TAG, "CLICK: Đã nhấn 'Cài đặt'")
            try {
                // Sử dụng action ID từ nav_graph.xml
                findNavController().navigate(R.id.action_accountFragment_to_settingsFragment)
            } catch (e: Exception) {
                Log.e(TAG, "LỖI: không thể điều hướng đến settingsFragment", e)
            }
        }
    }
}