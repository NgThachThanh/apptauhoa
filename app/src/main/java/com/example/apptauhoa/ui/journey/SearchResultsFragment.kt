package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SearchResultsFragment : Fragment() {

    // --- Data Properties ---
    private val originName = "Sài Gòn"
    private val destinationName = "Nha Trang"
    private val departureDate: LocalDate = LocalDate.now()
    private val totalPassengers = 1
    
    private val allTrips = createMockTrainTrips()
    private lateinit var trainScheduleAdapter: TrainScheduleAdapter

    // --- View Properties ---
    private lateinit var routeTitle: TextView
    private lateinit var routeSubtitle: TextView
    private lateinit var trainListRv: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        parentFragmentManager.setFragmentResultListener("filter_result", this) { _, bundle ->
            val minPrice = bundle.getFloat("minPrice")
            val maxPrice = bundle.getFloat("maxPrice")
            
            val filteredTrips = allTrips.filter { it.price >= minPrice && it.price <= maxPrice }
            showResults(filteredTrips)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)
        
        routeTitle = view.findViewById(R.id.txt_route_title)
        routeSubtitle = view.findViewById(R.id.txt_route_subtitle)
        trainListRv = view.findViewById(R.id.rv_train_list)
        progressBar = view.findViewById(R.id.progress_bar)
        statusText = view.findViewById(R.id.txt_status)

        setupRecyclerView()
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        loadData()
    }

    private fun setupHeader() {
        routeTitle.text = "$originName – $destinationName"
        val formatter = DateTimeFormatter.ofPattern("'Khởi hành' E, dd/MM/yyyy", Locale("vi", "VN"))
        routeSubtitle.text = "${departureDate.format(formatter)} • $totalPassengers khách"
        
        view?.findViewById<View>(R.id.btn_back)?.setOnClickListener { findNavController().popBackStack() }
        view?.findViewById<View>(R.id.btn_filter)?.setOnClickListener {
            findNavController().navigate(R.id.action_search_results_to_trip_filter)
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
                tripDate = trainTrip.tripDate
            )
            findNavController().navigate(action)
        }
        trainListRv.layoutManager = LinearLayoutManager(context)
        trainListRv.adapter = trainScheduleAdapter
    }
    
    private fun loadData() {
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({ showResults(allTrips) }, 1500)
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        statusText.visibility = View.GONE
        trainListRv.visibility = View.GONE
    }

    private fun showResults(trips: List<TrainTrip>) {
        progressBar.visibility = View.GONE
        if (trips.isEmpty()) {
            statusText.text = "Không tìm thấy chuyến tàu nào phù hợp."
            statusText.visibility = View.VISIBLE
        } else {
            statusText.visibility = View.GONE
            trainScheduleAdapter.updateData(trips)
        }
        trainListRv.visibility = if (trips.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun createMockTrainTrips(): List<TrainTrip> {
        return listOf(
            TrainTrip("ID01", "SE8", "Ngồi mềm điều hòa", 15, "19:30", "04:10", "8h 40m", "Sài Gòn", "Nha Trang", 350000L, departureDate.toString()),
            TrainTrip("ID02", "SNT2", "Giường nằm khoang 4", 8, "20:00", "05:00", "9h 0m", "Sài Gòn", "Nha Trang", 620000L, departureDate.toString()),
            TrainTrip("ID03", "SE6", "Ngồi cứng", 0, "21:05", "06:30", "9h 25m", "Sài Gòn", "Nha Trang", 280000L, departureDate.toString())
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
}