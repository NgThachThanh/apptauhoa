package com.example.apptauhoa.ui.lookup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.apptauhoa.R // Cần import R để lấy ID của RadioButton
import com.example.apptauhoa.databinding.FragmentLookupBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LookupFragment : Fragment() {

    private var _binding: FragmentLookupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // THÊM MỚI: Xử lý sự kiện khi người dùng thay đổi lựa chọn tra cứu
        binding.radioGroupLookupType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioButtonBookingCode) {
                binding.textInputLayout.hint = "Nhập mã đặt chỗ"
            } else {
                binding.textInputLayout.hint = "Nhập họ và tên"
            }
        }

        binding.buttonLookup.setOnClickListener {
            val lookupValue = binding.editTextLookupValue.text.toString().trim()
            if (lookupValue.isNotEmpty()) {
                // Lấy kiểu tra cứu đang được chọn
                val isLookupByCode = binding.radioGroupLookupType.checkedRadioButtonId == R.id.radioButtonBookingCode
                performLookup(lookupValue, isLookupByCode)
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập thông tin tra cứu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Hàm giả lập việc tra cứu thông tin vé.
     * @param lookupValue Giá trị người dùng nhập (mã vé hoặc họ tên).
     * @param isLookupByCode True nếu tra cứu theo mã, False nếu tra cứu theo họ tên.
     */
    private fun performLookup(lookupValue: String, isLookupByCode: Boolean) {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.buttonLookup.isEnabled = false
            binding.textViewResult.text = ""

            delay(2000) // Giả lập độ trễ mạng

            binding.progressBar.isVisible = false
            binding.buttonLookup.isEnabled = true

            // CẬP NHẬT: Logic tra cứu được mở rộng
            val result = if (isLookupByCode) {
                // Logic tra cứu theo Mã đặt chỗ
                when (lookupValue.uppercase()) {
                    "ABC1234" -> "Vé hợp lệ.\nChuyến tàu: SE1\nGa đi: Hà Nội\nGa đến: Sài Gòn\nTrạng thái: Đã thanh toán."
                    "XYZ5678" -> "Vé hợp lệ.\nChuyến tàu: TN5\nGa đi: Đà Nẵng\nGa đến: Nha Trang\nTrạng thái: Chưa thanh toán."
                    else -> "Mã đặt chỗ không hợp lệ hoặc không tìm thấy."
                }
            } else {
                // Logic tra cứu theo Họ và tên
                when (lookupValue.lowercase()) { // Chuyển về chữ thường để dễ so sánh
                    "nguyễn văn a" -> "Tìm thấy 1 vé.\nHành khách: Nguyễn Văn A\nChuyến tàu: SE1\nMã vé: ABC1234"
                    "trần thị b" -> "Tìm thấy 2 vé. Vui lòng cung cấp thêm thông tin để xác định vé chính xác."
                    else -> "Không tìm thấy hành khách nào có tên này."
                }
            }
            binding.textViewResult.text = result
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
