package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val datePicker = view.findViewById<DatePicker>(R.id.date_picker)
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)

        datePicker.minDate = System.currentTimeMillis() - 1000

        confirmButton.setOnClickListener {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            // Format to a safe string to send back
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dateString = formatter.format(calendar.time)

            setFragmentResult("date_result", bundleOf("selected_date" to dateString))
            findNavController().popBackStack()
        }

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}