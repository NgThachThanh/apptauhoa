package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.example.apptauhoa.R
import java.text.NumberFormat
import java.util.Locale

class TripFilterFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeSlider: RangeSlider = view.findViewById(R.id.slider_departure_time)
        val timeRangeText: TextView = view.findViewById(R.id.txt_time_range)
        val priceSlider: RangeSlider = view.findViewById(R.id.slider_price)
        val priceRangeText: TextView = view.findViewById(R.id.txt_price_range)
        
        // Initial text setup
        updateTimeRangeText(timeSlider, timeRangeText)
        updatePriceRangeText(priceSlider, priceRangeText)

        timeSlider.addOnChangeListener { slider, _, _ ->
            updateTimeRangeText(slider, timeRangeText)
        }
        
        priceSlider.addOnChangeListener { slider, _, _ ->
            updatePriceRangeText(slider, priceRangeText)
        }

        view.findViewById<Button>(R.id.btn_reset_filters).setOnClickListener {
            resetFilters(view)
        }

        view.findViewById<Button>(R.id.btn_apply_filters).setOnClickListener {
            applyFilters(view)
        }
    }
    
    private fun updateTimeRangeText(slider: RangeSlider, textView: TextView) {
        val minTime = slider.values[0]
        val maxTime = slider.values[1]
        textView.text = String.format("%02d:00 – %02d:00", minTime.toInt(), maxTime.toInt())
    }
    
    private fun updatePriceRangeText(slider: RangeSlider, textView: TextView) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val minPrice = slider.values[0]
        val maxPrice = slider.values[1]
        textView.text = "${currencyFormat.format(minPrice)} – ${currencyFormat.format(maxPrice)}"
    }

    private fun resetFilters(view: View) {
        view.findViewById<RangeSlider>(R.id.slider_departure_time).setValues(0f, 24.0f)
        view.findViewById<RangeSlider>(R.id.slider_price).setValues(100000f, 1000000f)
        view.findViewById<ChipGroup>(R.id.chip_coach_class).clearCheck()
        view.findViewById<RadioButton>(R.id.radio_all).isChecked = true
    }

    private fun applyFilters(view: View) {
        val timeSlider: RangeSlider = view.findViewById(R.id.slider_departure_time)
        val priceSlider: RangeSlider = view.findViewById(R.id.slider_price)
        val coachChipGroup: ChipGroup = view.findViewById(R.id.chip_coach_class)
        val availabilityRadioGroup: RadioGroup = view.findViewById(R.id.radio_availability)

        val selectedCoachClasses = coachChipGroup.checkedChipIds.map {
            view.findViewById<Chip>(it).text.toString()
        }
        
        val selectedRadioButtonId = availabilityRadioGroup.checkedRadioButtonId
        val availabilityStatus = view.findViewById<RadioButton>(selectedRadioButtonId).text.toString()

        setFragmentResult("filter_result", bundleOf(
            "minTime" to timeSlider.values[0],
            "maxTime" to timeSlider.values[1],
            "coachClasses" to selectedCoachClasses,
            "minPrice" to priceSlider.values[0],
            "maxPrice" to priceSlider.values[1],
            "availabilityStatus" to availabilityStatus
        ))
        
        dismiss()
    }

    companion object {
        const val TAG = "TripFilterFragment"
    }
}