package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

class DatePickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_date_picker, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val datePicker: DatePicker = view.findViewById(R.id.date_picker)
        val confirmButton: Button = view.findViewById(R.id.confirm_button)
        val cancelButton: Button = view.findViewById(R.id.cancel_button)

        toolbar.title = "Chọn ngày khởi hành"
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        // Set date limits
        val today = Calendar.getInstance()
        datePicker.minDate = today.timeInMillis

        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 1)
        datePicker.maxDate = maxDate.timeInMillis

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        confirmButton.setOnClickListener {
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth
            
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            val result = bundleOf("selected_date" to selectedDate.toString())
            
            setFragmentResult("date_result", result)
            findNavController().popBackStack()
        }

        return view
    }
}