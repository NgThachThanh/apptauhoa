package com.example.apptauhoa.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.example.apptauhoa.data.model.Station
import com.example.apptauhoa.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var promotionAdapter: PromotionAdapter

    private var originStation: Station? = null
    private var destinationStation: Station? = null
    private var departureDate: LocalDate? = LocalDate.now()
    private var adults = 1
    private var children = 0
    private var infants = 0

    private val welcomeMessages = listOf(
        "Chào mừng bạn đến với nhà ga",
        "Chúc bạn có một chuyến đi vui vẻ",
        "Khám phá những vùng đất mới",
        "Tận hưởng hành trình của bạn"
    )

    private val promotionImages = listOf(
        R.drawable.hinh_8,
        R.drawable.hinh_9,
        R.drawable.hinh_10,
        R.drawable.hinh_11
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupResultListeners()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupViewListeners()
        setupObservers()
        updateAllUI()

        binding.textWelcome.text = welcomeMessages.random()
        loadDummyPromotions()
    }

    private fun setupRecyclerViews() {
        promotionAdapter = PromotionAdapter { promotion: Promotion ->
            val action = HomeFragmentDirections.actionHomeToPromotionDetail(promotionId = promotion.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewPromotions.adapter = promotionAdapter
    }

    private fun setupObservers() {
        viewModel.randomSuggestion.observe(viewLifecycleOwner) { suggestion ->
            binding.textViewSuggestionTitle.text = suggestion.title
            binding.imageViewSuggestionBanner.setImageResource(suggestion.imageResId)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestionEvent.collect { 
                    destinationStation = Station(it.stationCode, it.stationName)
                    updateAllUI()
                    binding.nestedScrollViewHome.smoothScrollTo(0, 0)
                    binding.destinationValue.requestFocus()
                }
            }
        }
    }

    private fun loadDummyPromotions() {
        val promotions = listOf(
            Promotion("1", "Vé tàu Tết 2025", promotionImages[0], "Nội dung chi tiết..."),
            Promotion("2", "Chào hè sôi động", promotionImages[1], "Nội dung chi tiết..."),
            Promotion("3", "Giảm giá sinh viên", promotionImages[2], "Nội dung chi tiết..."),
            Promotion("4", "Đi nhóm 4 người", promotionImages[3], "Nội dung chi tiết...")
        )
        promotionAdapter.submitList(promotions)
    }

    private fun setupViewListeners() {
        binding.buttonFindTicketSuggestion.setOnClickListener { viewModel.onSuggestionCardClicked() }
        binding.textViewSeeAllPromotions.setOnClickListener { Toast.makeText(context, "Chức năng xem tất cả ưu đãi đang được phát triển!", Toast.LENGTH_SHORT).show() }
        binding.originStationRow.setOnClickListener { findNavController().navigate(R.id.action_home_to_station_picker, bundleOf("purpose" to "ORIGIN")) }
        binding.destinationStationRow.setOnClickListener { findNavController().navigate(R.id.action_home_to_station_picker, bundleOf("purpose" to "DESTINATION")) }
        binding.swapButton.setOnClickListener {
            val temp = originStation
            originStation = destinationStation
            destinationStation = temp
            updateAllUI()
        }
        binding.panelDepartureDate.setOnClickListener { findNavController().navigate(R.id.action_home_to_date_picker) }
        binding.panelPassengers.setOnClickListener {
            PassengersBottomSheetFragment.newInstance(adults, children, infants)
                .show(childFragmentManager, PassengersBottomSheetFragment.TAG)
        }
        binding.btnSearchTrips.setOnClickListener {
            if (validateForm(showErrors = true)) {
                val totalPassengers = adults + children
                val action = HomeFragmentDirections.actionHomeToSearchResults(
                    originName = originStation!!.name,
                    destinationName = destinationStation!!.name,
                    departureDate = departureDate!!.toString(),
                    adults = adults,
                    children = children,
                    infants = infants,
                    ticketCount = totalPassengers
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
    private fun updateStationUI() { binding.originValue.text = originStation?.name ?: "Nơi khởi hành"; binding.destinationValue.text = destinationStation?.name ?: "Quý khách muốn đi đâu?" }
    private fun updateDateUI() { if (departureDate != null) { val formatter = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale("vi", "VN")); binding.txtDepartureValue.text = departureDate!!.format(formatter) } else { binding.txtDepartureValue.text = "Chọn ngày" } }
    private fun updatePassengersUI() { val parts = mutableListOf<String>(); if (adults > 0) parts.add("$adults người lớn"); if (children > 0) parts.add("$children trẻ em"); if (infants > 0) parts.add("$infants sơ sinh"); binding.txtPassengersValue.text = if (parts.isEmpty()) "Chưa chọn hành khách" else parts.joinToString(", "); }
    private fun validateForm(showErrors: Boolean = false): Boolean { var isValid = true; val stationsSelected = originStation != null && destinationStation != null; val stationsNotSame = originStation != destinationStation; if (!stationsSelected || !stationsNotSame) { isValid = false; if (showErrors) { binding.stationErrorMessage.visibility = View.VISIBLE; binding.stationErrorMessage.text = if (!stationsSelected) "Vui lòng chọn ga đi và ga đến." else "Ga đi và ga đến không được trùng nhau." } } else { binding.stationErrorMessage.visibility = View.GONE }; if (departureDate == null) { isValid = false; if (showErrors) { binding.txtDepartureError.visibility = View.VISIBLE } } else { binding.txtDepartureError.visibility = View.GONE }; if (adults < 1 || infants > adults) { isValid = false }; binding.btnSearchTrips.isEnabled = isValid; return isValid }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}