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
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.Coach
import com.example.apptauhoa.data.model.Trip
import com.example.apptauhoa.databinding.FragmentSearchResultsBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding

    private val args: SearchResultsFragmentArgs by navArgs()
    
    private lateinit var allTrips: List<Trip>
    private lateinit var trainScheduleAdapter: TrainScheduleAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        
        parentFragmentManager.setFragmentResultListener("filter_result", this) { _, bundle ->
            if (_binding == null) return@setFragmentResultListener
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
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadDataFromDb()

        setupRecyclerView()
        setupHeader()
    }

    private fun loadDataFromDb() {
        showLoading()
        
        Handler(Looper.getMainLooper()).postDelayed({
            val dateStr = args.departureDate
            
            var trips = dbHelper.searchTrips(args.originName, args.destinationName, dateStr)
            
            if (trips.isEmpty()) {
                dbHelper.mockTripsForSearch(args.originName, args.destinationName, dateStr)
                trips = dbHelper.searchTrips(args.originName, args.destinationName, dateStr)
            }
            
            allTrips = trips
            showResults(allTrips)
        }, 1000)
    }

    private fun setupHeader() {
        binding?.txtRouteTitle?.text = "${args.originName} – ${args.destinationName}"
        try {
            // The date format from HomeFragment is yyyy-MM-dd
            val date = LocalDate.parse(args.departureDate) 
            val outputFormatter = DateTimeFormatter.ofPattern("'Khởi hành' E, dd/MM/yyyy", Locale("vi", "VN"))
            binding?.txtRouteSubtitle?.text = "${date.format(outputFormatter)} • ${args.ticketCount} khách"
        } catch (e: Exception) {
            // Fallback if parsing fails for any reason
            binding?.txtRouteSubtitle?.text = "${args.departureDate} • ${args.ticketCount} khách"
        }
        
        binding?.btnBack?.setOnClickListener { findNavController().popBackStack() }
        binding?.btnFilter?.setOnClickListener {
            findNavController().navigate(SearchResultsFragmentDirections.actionSearchResultsToTripFilter())
        }
    }

    private fun setupRecyclerView() {
        trainScheduleAdapter = TrainScheduleAdapter(emptyList()) { trainTrip ->
            // Fetch real coaches from DB
            val realCoaches = dbHelper.getCoachesByTripId(trainTrip.id)
            
            // Fallback to mock if DB has no coaches (for old data)
            val coachesToPass = if (realCoaches.isNotEmpty()) {
                realCoaches
            } else {
                createMockCoachesForTrip(trainTrip)
            }

            val departureTimestamp = parseDateTimeToTimestamp(trainTrip.tripDate, trainTrip.departureTime)
            var arrivalTimestamp = parseDateTimeToTimestamp(trainTrip.tripDate, trainTrip.arrivalTime)

            if (arrivalTimestamp < departureTimestamp) {
                arrivalTimestamp += TimeUnit.DAYS.toMillis(1)
            }

            val action = SearchResultsFragmentDirections.actionSearchResultsToCoachPicker(
                tripId = trainTrip.id,
                trainCode = trainTrip.trainCode,
                departureTime = departureTimestamp,
                arrivalTime = arrivalTimestamp,
                coachList = coachesToPass.toTypedArray(),
                originStation = trainTrip.originStation,
                destinationStation = trainTrip.destinationStation,
                tripDate = trainTrip.tripDate,
                ticketCount = args.ticketCount
            )
            findNavController().navigate(action)
        }
        binding?.rvTrainList?.layoutManager = LinearLayoutManager(context)
        binding?.rvTrainList?.adapter = trainScheduleAdapter
    }

    private fun parseDateTimeToTimestamp(dateStr: String, timeStr: String): Long {
        return try {
            // The date format (dateStr) from the database is yyyy-MM-dd
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())
            val localDateTime = LocalDateTime.parse("$dateStr $timeStr", dateTimeFormatter)
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } catch (e: Exception) {
            0L
        }
    }

    private fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.txtStatus?.visibility = View.GONE
        binding?.rvTrainList?.visibility = View.GONE
    }

    private fun showResults(trips: List<Trip>) {
        if (_binding == null) return
        binding?.progressBar?.visibility = View.GONE
        if (trips.isEmpty()) {
            binding?.txtStatus?.text = "Không tìm thấy chuyến tàu nào phù hợp."
            binding?.txtStatus?.visibility = View.VISIBLE
        } else {
            binding?.txtStatus?.visibility = View.GONE
            trainScheduleAdapter.updateData(trips)
        }
        binding?.rvTrainList?.visibility = if (trips.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun createMockCoachesForTrip(trip: Trip): List<Coach> {
         return when (trip.classTitle) {
            "Ngồi mềm điều hòa" -> listOf(
                Coach("C01", "Toa 1", "Ngồi mềm", 5, 56, trip.price),
                Coach("C02", "Toa 2", "Ngồi mềm", 10, 56, trip.price)
            )
            "Giường nằm khoang 4" -> listOf(
                Coach("C05", "Toa 5", "Giường nằm khoang 4", 2, 28, trip.price),
                Coach("C06", "Toa 6", "Giường nằm khoang 4", 6, 28, trip.price)
            )
            else -> listOf(
                 Coach("C01", "Toa 1", "Ghế ngồi", 20, 60, trip.price)
            )
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}