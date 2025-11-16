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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var promotionAdapter: PromotionAdapter

    private var originStation: Station? = null
    private var destinationStation: Station? = null
    private var departureDate: Calendar? = null
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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDefaultDate()
        setupViewListeners()
        setupSuggestionCard()
        setupPromotions()
    }

    private fun setupDefaultDate() {
        viewModel.defaultDepartureDate.observe(viewLifecycleOwner) { (date, formattedDate) ->
            if (departureDate == null) {
                departureDate = date
                updateAllUI()
            }
        }
    }

    private fun setupPromotions() {
        promotionAdapter = PromotionAdapter { }
        binding.recyclerViewPromotions.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = promotionAdapter
        }
        viewModel.promotions.observe(viewLifecycleOwner) { promotions ->
            promotionAdapter.submitList(promotions)
        }
    }

    private fun setupSuggestionCard() {
        viewModel.randomSuggestion.observe(viewLifecycleOwner) { suggestion ->
            binding.textViewSuggestionTitle.text = suggestion.title
        }
        binding.buttonFindTicketSuggestion.setOnClickListener {
            viewModel.onSuggestionCardClicked()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestionEvent.collect { suggestion ->
                    binding.destinationValue.text = suggestion.stationName
                    binding.nestedScrollViewHome.smoothScrollTo(0, 0)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupResultListeners() {
        childFragmentManager.setFragmentResultListener("passengers_result", this) { _, bundle ->
            adults = bundle.getInt("adults")
            children = bundle.getInt("children")
            infants = bundle.getInt("infants")
            updateAllUI()
        }
        parentFragmentManager.setFragmentResultListener("ORIGIN", this) { _, bundle ->
             originStation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("station", Station::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable("station")
            }
            updateAllUI()
        }
        parentFragmentManager.setFragmentResultListener("DESTINATION", this) { _, bundle ->
             destinationStation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("station", Station::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable("station")
            }
            updateAllUI()
        }
        parentFragmentManager.setFragmentResultListener("date_result", this) { _, bundle ->
            val dateString = bundle.getString("selected_date")
            if (dateString != null) {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString)
                if (date != null) {
                    departureDate = Calendar.getInstance().apply { time = date }
                    updateAllUI()
                }
            }
        }
    }

    private fun setupViewListeners() {
        binding.originStationRow.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToStationPicker("ORIGIN"))
        }
        binding.destinationStationRow.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToStationPicker("DESTINATION"))
        }
        binding.swapButton.setOnClickListener {
            val temp = originStation
            originStation = destinationStation
            destinationStation = temp
            updateAllUI()
        }
        binding.panelDepartureDate.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToDatePicker())
        }
        binding.panelPassengers.setOnClickListener {
            PassengersBottomSheetFragment.newInstance(adults, children, infants)
                .show(childFragmentManager, PassengersBottomSheetFragment.TAG)
        }
        binding.btnSearchTrips.setOnClickListener {
            if (validateForm(showErrors = true)) {
                val dateString = departureDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it.time)
                } ?: ""
                val ticketCount = adults + children

                val action = HomeFragmentDirections.actionHomeToSearchResults(
                    originName = originStation!!.name,
                    destinationName = destinationStation!!.name,
                    departureDate = dateString,
                    adults = adults,
                    children = children,
                    infants = infants,
                    ticketCount = ticketCount
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun updateAllUI() {
        updateStationUI()
        updateDateUI()
        updatePassengersUI()
        validateForm()
    }
    
    // Other update UI and validation methods...
    private fun updateStationUI() {
        binding.originValue.text = originStation?.name ?: "Nơi khởi hành"
        binding.destinationValue.text = destinationStation?.name ?: "Quý khách muốn đi đâu?"
    }

    private fun updateDateUI() {
        binding.txtDepartureValue.text = departureDate?.let {
            SimpleDateFormat("EEE, dd/MM/yyyy", Locale("vi", "VN")).format(it.time)
        } ?: "Chọn ngày"
    }

    private fun updatePassengersUI() {
        val parts = mutableListOf<String>()
        if (adults > 0) parts.add("$adults người lớn")
        if (children > 0) parts.add("$children trẻ em")
        if (infants > 0) parts.add("$infants sơ sinh")
        binding.txtPassengersValue.text = if (parts.isEmpty()) "Chọn hành khách" else parts.joinToString(", ")
    }
    
    private fun validateForm(showErrors: Boolean = false): Boolean {
        var isValid = true
        val stationsSelected = originStation != null && destinationStation != null
        val stationsNotSame = originStation != destinationStation
        if (!stationsSelected || !stationsNotSame) {
            isValid = false
            if (showErrors) {
                binding.stationErrorMessage.visibility = View.VISIBLE
                binding.stationErrorMessage.text = if (!stationsSelected) "Vui lòng chọn ga đi và ga đến." else "Ga đi và ga đến không được trùng nhau."
            }
        } else {
            binding.stationErrorMessage.visibility = View.GONE
        }
        if (departureDate == null) {
            isValid = false
            if (showErrors) {
                binding.txtDepartureError.visibility = View.VISIBLE
            }
        } else {
            binding.txtDepartureError.visibility = View.GONE
        }
        if (adults < 1) {
            isValid = false
        }
        binding.btnSearchTrips.isEnabled = isValid
        return isValid
    }
}