// Corrected SearchResultsFragment.kt
package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.databinding.FragmentSearchResultsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!

    private val args: SearchResultsFragmentArgs by navArgs()
    
    private lateinit var allTrips: List<TrainTrip>
    private lateinit var trainScheduleAdapter: TrainScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        parentFragmentManager.setFragmentResultListener("filter_result", this) { _, bundle ->
            val minPrice = bundle.getFloat("minPrice")
            val maxPrice = bundle.getFloat("maxPrice")
            
            if (::allTrips.isInitialized) {
                val filteredTrips = allTrips.filter { it.price >= minPrice && it.price <= maxPrice }
                showResults(filteredTrips)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allTrips = createMockTrainTrips()

        setupRecyclerView()
        setupHeader()
        loadData()
    }

    private fun setupHeader() {
        binding.txtRouteTitle.text = "${args.originName} – ${args.destinationName}"
        val formatter = DateTimeFormatter.ofPattern("'Khởi hành' E, dd/MM/yyyy", Locale("vi", "VN"))
        val date = LocalDate.parse(args.departureDate)
        binding.txtRouteSubtitle.text = "${date.format(formatter)} • ${args.ticketCount} khách"
        
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(SearchResultsFragmentDirections.actionSearchResultsToTripFilter())
        }
    }

    private fun setupRecyclerView() {
        trainScheduleAdapter = TrainScheduleAdapter(emptyList()) { trainTrip ->
            val mockCoaches = createMockCoachesForTrip(trainTrip)
            val action = SearchResultsFragmentDirections.actionSearchResultsToCoachPicker(
                tripId = trainTrip.tripId,
                trainCode = trainTrip.trainCode,
                departureTime = trainTrip.departureTime,
                arrivalTime = trainTrip.arrivalTime,
                coachList = mockCoaches.toTypedArray(),
                originStation = trainTrip.originStation,
                destinationStation = trainTrip.destinationStation,
                tripDate = trainTrip.tripDate,
                ticketCount = args.ticketCount
            )
            findNavController().navigate(action)
        }
        binding.rvTrainList.layoutManager = LinearLayoutManager(context)
        binding.rvTrainList.adapter = trainScheduleAdapter
    }
    
    private fun loadData() {
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({ showResults(allTrips) }, 1500)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtStatus.visibility = View.GONE
        binding.rvTrainList.visibility = View.GONE
    }

    private fun showResults(trips: List<TrainTrip>) {
        binding.progressBar.visibility = View.GONE
        if (trips.isEmpty()) {
            binding.txtStatus.text = "Không tìm thấy chuyến tàu nào phù hợp."
            binding.txtStatus.visibility = View.VISIBLE
        } else {
            binding.txtStatus.visibility = View.GONE
            trainScheduleAdapter.updateData(trips)
        }
        binding.rvTrainList.visibility = if (trips.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun createMockTrainTrips(): List<TrainTrip> {
         val date = LocalDate.parse(args.departureDate)
        return listOf(
            TrainTrip("ID01", "SE8", "Ngồi mềm điều hòa", 15, "19:30", "04:10", "8h 40m", args.originName, args.destinationName, 350000L, date.toString()),
            TrainTrip("ID02", "SNT2", "Giường nằm khoang 4", 8, "20:00", "05:00", "9h 0m", args.originName, args.destinationName, 620000L, date.toString()),
            TrainTrip("ID03", "SE6", "Ngồi cứng", 0, "21:05", "06:30", "9h 25m", args.originName, args.destinationName, 280000L, date.toString())
        )
    }

    private fun createMockCoachesForTrip(trip: TrainTrip): List<Coach> {
         return when (trip.coachClass) {
            "Ngồi mềm điều hòa" -> listOf(
                Coach("C01", "Toa 1", "Ngồi mềm", 5, 56, 350000L),
                Coach("C02", "Toa 2", "Ngồi mềm", 10, 56, 350000L)
            )
            "Giường nằm khoang 4" -> listOf(
                Coach("C05", "Toa 5", "Giường nằm khoang 4", 2, 28, 620000L),
                Coach("C06", "Toa 6", "Giường nằm khoang 4", 6, 28, 650000L)
            )
            else -> emptyList()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
