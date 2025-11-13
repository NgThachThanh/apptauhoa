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
import com.google.android.material.appbar.MaterialToolbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SearchResultsFragment : Fragment() {

    private lateinit var trainScheduleAdapter: TrainScheduleAdapter

    private lateinit var toolbar: MaterialToolbar
    private lateinit var routeTitle: TextView
    private lateinit var routeSubtitle: TextView
    private lateinit var dateSelectorRv: RecyclerView
    private lateinit var trainListRv: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    
    // Mock data - replace with NavArgs later
    private val originName = "Sài Gòn"
    private val destinationName = "Nha Trang"
    private val departureDate = LocalDate.now()
    private val totalPassengers = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)
        
        // Initialize views
        routeTitle = view.findViewById(R.id.txt_route_title)
        routeSubtitle = view.findViewById(R.id.txt_route_subtitle)
        dateSelectorRv = view.findViewById(R.id.rv_date_selector)
        trainListRv = view.findViewById(R.id.rv_train_list)
        progressBar = view.findViewById(R.id.progress_bar)
        statusText = view.findViewById(R.id.txt_status)

        setupHeader()
        setupRecyclerView()
        
        // Simulate loading data
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            showResults(createMockTrainTrips())
        }, 1500)
        
        return view
    }

    private fun setupHeader() {
        routeTitle.text = "$originName – $destinationName"
        val formatter = DateTimeFormatter.ofPattern("'Khởi hành' E, dd/MM/yyyy", Locale("vi", "VN"))
        routeSubtitle.text = "${departureDate.format(formatter)} • $totalPassengers khách"
        
        view?.findViewById<View>(R.id.btn_back)?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        trainScheduleAdapter = TrainScheduleAdapter(emptyList()) { trainTrip ->
            // Navigate to SeatSelectionFragment with arguments
            // val action = SearchResultsFragmentDirections.actionSearchResultsToSeatSelection(
            //     trainCode = trainTrip.trainCode,
            //     // ... pass other args
            // )
            // findNavController().navigate(action)
        }
        trainListRv.layoutManager = LinearLayoutManager(context)
        trainListRv.adapter = trainScheduleAdapter
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        statusText.visibility = View.GONE
        trainListRv.visibility = View.GONE
    }

    private fun showResults(trips: List<TrainTrip>) {
        progressBar.visibility = View.GONE
        if (trips.isEmpty()) {
            statusText.text = "Không tìm thấy chuyến tàu nào."
            statusText.visibility = View.VISIBLE
            trainListRv.visibility = View.GONE
        } else {
            statusText.visibility = View.GONE
            trainListRv.visibility = View.VISIBLE
            trainScheduleAdapter.updateData(trips)
        }
    }

    private fun createMockTrainTrips(): List<TrainTrip> {
        return listOf(
            TrainTrip("ID01", "SE8", "Ngồi mềm điều hòa", 15, "19:30", "04:10", "8h 40m", "Sài Gòn", "Nha Trang", 350000L, departureDate.toString()),
            TrainTrip("ID02", "SNT2", "Giường nằm khoang 4", 8, "20:00", "05:00", "9h 0m", "Sài Gòn", "Nha Trang", 620000L, departureDate.toString()),
            TrainTrip("ID03", "SE6", "Ngồi cứng", 45, "21:05", "06:30", "9h 25m", "Sài Gòn", "Nha Trang", 280000L, departureDate.toString()),
            TrainTrip("ID04", "SE22", "Giường nằm khoang 6", 12, "22:00", "08:15", "10h 15m", "Sài Gòn", "Nha Trang", 510000L, departureDate.toString())
        )
    }
}