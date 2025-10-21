package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.apptauhoa.R

class PassengersBottomSheetFragment : BottomSheetDialogFragment() {

    private var adults = 1
    private var children = 0
    private var infants = 0

    private lateinit var adultsCountTextView: TextView
    private lateinit var childrenCountTextView: TextView
    private lateinit var infantsCountTextView: TextView
    private lateinit var errorTextView: TextView
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adults = it.getInt(ARG_ADULTS, 1)
            children = it.getInt(ARG_CHILDREN, 0)
            infants = it.getInt(ARG_INFANTS, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_passengers, container, false)

        adultsCountTextView = view.findViewById(R.id.adults_count_text_view)
        childrenCountTextView = view.findViewById(R.id.children_count_text_view)
        infantsCountTextView = view.findViewById(R.id.infants_count_text_view)
        errorTextView = view.findViewById(R.id.txt_passengers_error)
        confirmButton = view.findViewById(R.id.confirm_button)

        setupInitialValues()
        setupClickListeners(view)
        validatePassengerCounts()

        return view
    }

    private fun setupInitialValues() {
        adultsCountTextView.text = adults.toString()
        childrenCountTextView.text = children.toString()
        infantsCountTextView.text = infants.toString()
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<ImageButton>(R.id.adults_minus_button).setOnClickListener { updateCount(CounterType.ADULTS, -1) }
        view.findViewById<ImageButton>(R.id.adults_plus_button).setOnClickListener { updateCount(CounterType.ADULTS, 1) }
        view.findViewById<ImageButton>(R.id.children_minus_button).setOnClickListener { updateCount(CounterType.CHILDREN, -1) }
        view.findViewById<ImageButton>(R.id.children_plus_button).setOnClickListener { updateCount(CounterType.CHILDREN, 1) }
        view.findViewById<ImageButton>(R.id.infants_minus_button).setOnClickListener { updateCount(CounterType.INFANTS, -1) }
        view.findViewById<ImageButton>(R.id.infants_plus_button).setOnClickListener { updateCount(CounterType.INFANTS, 1) }

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener { dismiss() }
        confirmButton.setOnClickListener {
            val result = bundleOf(
                "adults" to adults,
                "children" to children,
                "infants" to infants
            )
            setFragmentResult("passengers_result", result)
            dismiss()
        }
    }
    
    private fun updateCount(type: CounterType, delta: Int) {
        when(type) {
            CounterType.ADULTS -> adults = (adults + delta).coerceIn(1, 6)
            CounterType.CHILDREN -> children = (children + delta).coerceIn(0, 6)
            CounterType.INFANTS -> infants = (infants + delta).coerceIn(0, 6)
        }
        setupInitialValues()
        validatePassengerCounts()
    }

    private fun validatePassengerCounts() {
        val total = adults + children + infants
        val infantsExceedAdults = infants > adults
        
        // Disable plus buttons if total is maxed out
        view?.findViewById<ImageButton>(R.id.adults_plus_button)?.isEnabled = total < 9 && adults < 6
        view?.findViewById<ImageButton>(R.id.children_plus_button)?.isEnabled = total < 9 && children < 6
        view?.findViewById<ImageButton>(R.id.infants_plus_button)?.isEnabled = total < 9 && infants < 6

        // Show error and disable confirm button if infants > adults
        if (infantsExceedAdults) {
            errorTextView.visibility = View.VISIBLE
            confirmButton.isEnabled = false
        } else {
            errorTextView.visibility = View.GONE
            confirmButton.isEnabled = true
        }
    }

    private enum class CounterType { ADULTS, CHILDREN, INFANTS }
    
    companion object {
        const val TAG = "PassengersBottomSheet"
        private const val ARG_ADULTS = "adults"
        private const val ARG_CHILDREN = "children"
        private const val ARG_INFANTS = "infants"

        fun newInstance(adults: Int, children: Int, infants: Int): PassengersBottomSheetFragment {
            return PassengersBottomSheetFragment().apply {
                arguments = bundleOf(
                    ARG_ADULTS to adults,
                    ARG_CHILDREN to children,
                    ARG_INFANTS to infants
                )
            }
        }
    }
}
