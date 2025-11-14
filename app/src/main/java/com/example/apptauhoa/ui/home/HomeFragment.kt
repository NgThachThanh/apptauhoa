package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    // Data state
    private var originStation: Station? = null
    private var destinationStation: Station? = null
    private var departureDate: LocalDate? = null
    private var adults = 1
    private var children = 0
    private var infants = 0

    // Views
    private lateinit var originValue: TextView
    private lateinit var destinationValue: TextView
    private lateinit var stationError: TextView
    private lateinit var departureDateValue: TextView
    private lateinit var departureDateError: TextView
    private lateinit var passengersValue: TextView
    private lateinit var searchButton: Button
    private lateinit var passengersPanel: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Listeners should be set up here as they don't depend on the view
        setupResultListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        originValue = view.findViewById(R.id.origin_value)
        destinationValue = view.findViewById(R.id.destination_value)
        stationError = view.findViewById(R.id.station_error_message)
        departureDateValue = view.findViewById(R.id.txt_departure_value)
        departureDateError = view.findViewById(R.id.txt_departure_error)
        passengersValue = view.findViewById(R.id.txt_passengers_value)
        searchButton = view.findViewById(R.id.btn_search_trips)
        passengersPanel = view.findViewById(R.id.panel_passengers)
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Setup view listeners and initial UI state here
        setupViewListeners(view)
        updateAllUI()
    }
    
    private fun setupResultListeners() {
        childFragmentManager.setFragmentResultListener("passengers_result", this) { _, bundle ->
            adults = bundle.getInt("adults")
            children = bundle.getInt("children")
            infants = bundle.getInt("infants")
            updateAllUI()
        }
        
        parentFragmentManager.setFragmentResultListener("ORIGIN", this) { _, bundle ->
            originStation = bundle.getParcelable("station")
            updateAllUI()
        }
        parentFragmentManager.setFragmentResultListener("DESTINATION", this) { _, bundle ->
            destinationStation = bundle.getParcelable("station")
            updateAllUI()
        }
        parentFragmentManager.setFragmentResultListener("date_result", this) { _, bundle ->
            val dateString = bundle.getString("selected_date")
            if (dateString != null) {
                departureDate = LocalDate.parse(dateString)
                updateAllUI()
            }
        }
    }

    private fun setupViewListeners(view: View) {
        view.findViewById<ConstraintLayout>(R.id.origin_station_row).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_station_picker, bundleOf("purpose" to "ORIGIN"))
        }
        view.findViewById<ConstraintLayout>(R.id.destination_station_row).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_station_picker, bundleOf("purpose" to "DESTINATION"))
        }
        view.findViewById<ImageButton>(R.id.swap_button).setOnClickListener {
            val temp = originStation
            originStation = destinationStation
            destinationStation = temp
            updateAllUI()
        }
        view.findViewById<LinearLayout>(R.id.panel_departure_date).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_date_picker)
        }
        passengersPanel.setOnClickListener {
            val bottomSheet = PassengersBottomSheetFragment.newInstance(adults, children, infants)
            bottomSheet.show(childFragmentManager, PassengersBottomSheetFragment.TAG)
        }
        searchButton.setOnClickListener {
            if (validateForm(showErrors = true)) {
                val args = bundleOf(
                    "originName" to originStation!!.name,
                    "destinationName" to destinationStation!!.name,
                    "departureDate" to departureDate!!.toString(),
                    "adults" to adults,
                    "children" to children,
                    "infants" to infants
                )
                findNavController().navigate(R.id.action_home_to_search_results, args)
            }
        }
    }
    
    private fun updateAllUI() {
        updateStationUI()
        updateDateUI()
        updatePassengersUI()
        validateForm()
    }

    private fun updateStationUI() {
        originValue.text = originStation?.name ?: "Nơi khởi hành"
        destinationValue.text = destinationStation?.name ?: "Quý khách muốn đi đâu?"
    }

    private fun updateDateUI() {
        if (departureDate != null) {
            val formatter = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale("vi", "VN"))
            departureDateValue.text = departureDate!!.format(formatter)
        } else {
            departureDateValue.text = "Chọn ngày"
        }
    }
    
    private fun updatePassengersUI() {
        val parts = mutableListOf<String>()
        if (adults > 0) parts.add("$adults người lớn")
        if (children > 0) parts.add("$children trẻ em")
        if (infants > 0) parts.add("$infants sơ sinh")

        val passengersText = if (parts.isEmpty()) "Chưa chọn hành khách" else parts.joinToString(", ")
        passengersValue.text = passengersText
        
        val total = adults + children + infants
        val contentDescriptionText = "Hành khách. Hiện ${passengersValue.text}. (Tổng $total khách). Nhấn để thay đổi."
        passengersPanel.contentDescription = contentDescriptionText
    }

    private fun validateForm(showErrors: Boolean = false): Boolean {
        var isValid = true
        
        val stationsSelected = originStation != null && destinationStation != null
        val stationsNotSame = originStation != destinationStation
        if (!stationsSelected || !stationsNotSame) {
            isValid = false
            if (showErrors) {
                stationError.visibility = View.VISIBLE
                stationError.text = if (!stationsSelected) "Vui lòng chọn ga đi và ga đến." else "Ga đi và ga đến không được trùng nhau."
            }
        } else {
            stationError.visibility = View.GONE
        }
        
        if (departureDate == null) {
            isValid = false
            if (showErrors) {
                departureDateError.visibility = View.VISIBLE
            }
        } else {
            departureDateError.visibility = View.GONE
        }
        
        if (adults < 1 || infants > adults) {
            isValid = false
        }
        
        searchButton.isEnabled = isValid
        return isValid
    }
}