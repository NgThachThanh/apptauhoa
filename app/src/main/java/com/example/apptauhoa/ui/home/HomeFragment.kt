package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var promotionAdapter: PromotionAdapter
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var originValue: TextView
    private lateinit var destinationValue: TextView
    private lateinit var stationError: TextView
    private lateinit var departureDateValue: TextView
    private lateinit var departureDateError: TextView
    private lateinit var passengersValue: TextView
    private lateinit var searchButton: Button
    private lateinit var passengersPanel: LinearLayout
    private lateinit var promotionsRecyclerView: RecyclerView
    private lateinit var seeAllPromotionsButton: TextView
    private lateinit var suggestionSubtitle: TextView
    private lateinit var findTicketSuggestionButton: Button
    
    private var originStation: Station? = null
    private var destinationStation: Station? = null
    private var departureDate: LocalDate? = LocalDate.now()
    private var adults = 1
    private var children = 0
    private var infants = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupResultListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initializeViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewListeners(view)
        setupRecyclerViews()
        setupObservers()
        updateAllUI()
    }
    
    private fun initializeViews(view: View) {
        nestedScrollView = view.findViewById(R.id.nested_scroll_view_home)
        originValue = view.findViewById(R.id.origin_value)
        destinationValue = view.findViewById(R.id.destination_value)
        stationError = view.findViewById(R.id.station_error_message)
        departureDateValue = view.findViewById(R.id.txt_departure_value)
        departureDateError = view.findViewById(R.id.txt_departure_error)
        passengersValue = view.findViewById(R.id.txt_passengers_value)
        searchButton = view.findViewById(R.id.btn_search_trips)
        passengersPanel = view.findViewById(R.id.panel_passengers)
        promotionsRecyclerView = view.findViewById(R.id.recycler_view_promotions)
        seeAllPromotionsButton = view.findViewById(R.id.textView_see_all_promotions)
        suggestionSubtitle = view.findViewById(R.id.textView_suggestion_subtitle)
        findTicketSuggestionButton = view.findViewById(R.id.button_find_ticket_suggestion)
    }

    private fun setupRecyclerViews() {
        promotionAdapter = PromotionAdapter { promotion ->
            Toast.makeText(context, "Clicked on: ${promotion.title}", Toast.LENGTH_SHORT).show()
        }
        promotionsRecyclerView.adapter = promotionAdapter
    }

    private fun setupObservers() {
        viewModel.promotions.observe(viewLifecycleOwner) { promotions ->
            promotionAdapter.submitList(promotions)
        }
        viewModel.randomSuggestion.observe(viewLifecycleOwner) { suggestion ->
            suggestionSubtitle.text = suggestion.title
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestionEvent.collect { suggestion ->
                    destinationStation = Station(suggestion.stationCode, suggestion.stationName)
                    updateAllUI()
                    nestedScrollView.smoothScrollTo(0, 0)
                    destinationValue.requestFocus()
                }
            }
        }
    }

    private fun setupViewListeners(view: View) {
        findTicketSuggestionButton.setOnClickListener { viewModel.onSuggestionCardClicked() }
        seeAllPromotionsButton.setOnClickListener { Toast.makeText(context, "Chức năng xem tất cả ưu đãi đang được phát triển!", Toast.LENGTH_SHORT).show() }
        view.findViewById<ConstraintLayout>(R.id.origin_station_row).setOnClickListener { findNavController().navigate(R.id.action_home_to_station_picker, bundleOf("purpose" to "ORIGIN")) }
        view.findViewById<ConstraintLayout>(R.id.destination_station_row).setOnClickListener { findNavController().navigate(R.id.action_home_to_station_picker, bundleOf("purpose" to "DESTINATION")) }
        view.findViewById<ImageButton>(R.id.swap_button).setOnClickListener {
            val temp = originStation
            originStation = destinationStation
            destinationStation = temp
            updateAllUI()
        }
        view.findViewById<LinearLayout>(R.id.panel_departure_date).setOnClickListener { findNavController().navigate(R.id.action_home_to_date_picker) }
        passengersPanel.setOnClickListener {
            val bottomSheet = PassengersBottomSheetFragment.newInstance(adults, children, infants)
            bottomSheet.show(childFragmentManager, PassengersBottomSheetFragment.TAG)
        }
        searchButton.setOnClickListener {
            if (validateForm(showErrors = true)) {
                val totalPassengers = adults + children
                val action = HomeFragmentDirections.actionHomeToSearchResults(
                    originName = originStation!!.name,
                    destinationName = destinationStation!!.name,
                    departureDate = departureDate!!.toString(),
                    adults = adults,
                    children = children,
                    infants = infants,
                    totalPassengers = totalPassengers
                )
                findNavController().navigate(action)
            }
        }
    }
    
    private fun setupResultListeners() {
        childFragmentManager.setFragmentResultListener("passengers_result", this) { _, bundle -> adults = bundle.getInt("adults"); children = bundle.getInt("children"); infants = bundle.getInt("infants"); updateAllUI() }
        parentFragmentManager.setFragmentResultListener("ORIGIN", this) { _, bundle -> originStation = bundle.getParcelable("station"); updateAllUI() }
        parentFragmentManager.setFragmentResultListener("DESTINATION", this) { _, bundle -> destinationStation = bundle.getParcelable("station"); updateAllUI() }
        parentFragmentManager.setFragmentResultListener("date_result", this) { _, bundle -> val dateString = bundle.getString("selected_date"); if (dateString != null) { departureDate = LocalDate.parse(dateString); updateAllUI() } }
    }
    private fun updateAllUI() { updateStationUI(); updateDateUI(); updatePassengersUI(); validateForm() }
    private fun updateStationUI() { originValue.text = originStation?.name ?: "Nơi khởi hành"; destinationValue.text = destinationStation?.name ?: "Quý khách muốn đi đâu?" }
    private fun updateDateUI() { if (departureDate != null) { val formatter = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale("vi", "VN")); departureDateValue.text = departureDate!!.format(formatter) } else { departureDateValue.text = "Chọn ngày" } }
    private fun updatePassengersUI() { val parts = mutableListOf<String>(); if (adults > 0) parts.add("$adults người lớn"); if (children > 0) parts.add("$children trẻ em"); if (infants > 0) parts.add("$infants sơ sinh"); passengersValue.text = if (parts.isEmpty()) "Chưa chọn hành khách" else parts.joinToString(", "); }
    private fun validateForm(showErrors: Boolean = false): Boolean { var isValid = true; val stationsSelected = originStation != null && destinationStation != null; val stationsNotSame = originStation != destinationStation; if (!stationsSelected || !stationsNotSame) { isValid = false; if (showErrors) { stationError.visibility = View.VISIBLE; stationError.text = if (!stationsSelected) "Vui lòng chọn ga đi và ga đến." else "Ga đi và ga đến không được trùng nhau." } } else { stationError.visibility = View.GONE }; if (departureDate == null) { isValid = false; if (showErrors) { departureDateError.visibility = View.VISIBLE } } else { departureDateError.visibility = View.GONE }; if (adults < 1 || infants > adults) { isValid = false }; searchButton.isEnabled = isValid; return isValid }
}