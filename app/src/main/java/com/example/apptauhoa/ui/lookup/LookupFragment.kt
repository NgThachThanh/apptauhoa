package com.example.apptauhoa.ui.lookup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.databinding.FragmentLookupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LookupFragment : Fragment() {

    private var _binding: FragmentLookupBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroupLookupType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioButtonBookingCode) {
                binding.editTextLookupValue.hint = "Nhập mã đặt chỗ"
            } else {
                binding.editTextLookupValue.hint = "Nhập họ và tên"
            }
        }

        binding.buttonLookup.setOnClickListener {
            val lookupValue = binding.editTextLookupValue.text.toString().trim()
            if (lookupValue.isNotEmpty()) {
                val isLookupByCode = binding.radioGroupLookupType.checkedRadioButtonId == R.id.radioButtonBookingCode
                performLookup(lookupValue, isLookupByCode)
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập thông tin tra cứu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLookup(lookupValue: String, isLookupByCode: Boolean) {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.buttonLookup.isEnabled = false
            binding.textViewResult.text = ""

            val result = withContext(Dispatchers.IO) {
                if (isLookupByCode) {
                    val ticket = dbHelper.getTicketByBookingCode(lookupValue)
                    if (ticket != null) {
                        "Thông tin vé:\n" +
                                "Mã đặt chỗ: ${ticket.bookingCode}\n" +
                                "Chuyến tàu: ${ticket.trainCode}\n" +
                                "Ga đi: ${ticket.originStation}\n" +
                                "Ga đến: ${ticket.destinationStation}\n" +
                                "Ngày đi: ${ticket.tripDate}\n" +
                                "Ghế: ${ticket.selectedSeatsInfo}\n" +
                                "Trạng thái: ${ticket.status}"
                    } else {
                        "Không tìm thấy vé với mã đặt chỗ này."
                    }
                } else {
                    val tickets = dbHelper.getTicketsByPassengerName(lookupValue)
                    if (tickets.isNotEmpty()) {
                        // For simplicity, just show info for the first ticket found
                        val ticket = tickets[0]
                        "Tìm thấy ${tickets.size} vé. Hiển thị vé đầu tiên:\n" +
                                "Mã đặt chỗ: ${ticket.bookingCode}\n" +
                                "Chuyến tàu: ${ticket.trainCode}\n" +
                                "Ga đi: ${ticket.originStation}\n" +
                                "Ga đến: ${ticket.destinationStation}"
                    } else {
                        "Không tìm thấy vé nào cho hành khách này."
                    }
                }
            }

            binding.progressBar.isVisible = false
            binding.buttonLookup.isEnabled = true
            binding.textViewResult.text = result
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
