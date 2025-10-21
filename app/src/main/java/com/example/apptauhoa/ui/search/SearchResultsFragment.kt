package com.example.apptauhoa.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.example.apptauhoa.ui.ticket.Coach
import com.example.apptauhoa.ui.ticket.Seat
import com.example.apptauhoa.ui.ticket.SeatStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SearchResultsFragment : Fragment() {

    private lateinit var originName: String
    private lateinit var destinationName: String
    private lateinit var departureDate: String
    private var adults: Int = 0
    private var children: Int = 0
    private var infants: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            originName = it.getString("originName", "")
            destinationName = it.getString("destinationName", "")
            departureDate = it.getString("departureDate", "")
            adults = it.getInt("adults", 0)
            children = it.getInt("children", 0)
            infants = it.getInt("infants", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        
        val routeTitle: TextView = view.findViewById(R.id.route_title)
        val departureDateSubtitle: TextView = view.findViewById(R.id.departure_date_subtitle)
        routeTitle.text = "$originName – $destinationName"
        val parsedDate = LocalDate.parse(departureDate)
        val formatter = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale("vi", "VN"))
        val totalPassengers = adults + children + infants
        departureDateSubtitle.text = "Khởi hành ${parsedDate.format(formatter)} • $totalPassengers khách"
        
        val progressBar: ProgressBar = view.findViewById(R.id.progress_loading)
        val emptyView: TextView = view.findViewById(R.id.empty_view)
        val dateRail: RecyclerView = view.findViewById(R.id.date_rail)
        val tripsRecyclerView: RecyclerView = view.findViewById(R.id.rv_trips)

        progressBar.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        dateRail.visibility = View.GONE
        tripsRecyclerView.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            val mockTrips = getMockTrips()
            
            progressBar.visibility = View.GONE
            if (mockTrips.isEmpty()) {
                emptyView.visibility = View.VISIBLE
            } else {
                dateRail.visibility = View.VISIBLE
                tripsRecyclerView.visibility = View.VISIBLE
                
                dateRail.layoutManager = LinearLayoutManager(context)
                dateRail.adapter = DateRailAdapter(getMockDates(parsedDate))
                
                tripsRecyclerView.layoutManager = LinearLayoutManager(context)
                tripsRecyclerView.adapter = TripsAdapter(mockTrips) { trip ->
                    val coaches = ArrayList(getMockCoaches())
                    val args = bundleOf(
                        "tripId" to trip.id,
                        "originName" to originName,
                        "destinationName" to destinationName,
                        "departureDate" to departureDate,
                        "adults" to adults,
                        "children" to children,
                        "infants" to infants,
                        "basePrice" to 250000,
                        "seatMapType" to "SEAT_2x2",
                        "coaches" to coaches
                    )
                    findNavController().navigate(R.id.action_search_results_to_seat_selection, args)
                }
            }
        }, 1500)

        return view
    }

    private fun getMockDates(centerDate: LocalDate): List<LocalDate> = (-2..4).map { centerDate.plusDays(it.toLong()) }
    
    private fun getMockTrips(): List<Trip> = listOf(
        Trip("ID123", "Giường nằm khoang 4", 8, "05:00", "07:40", "2h 40m", "Ga Sài Gòn", "Ga Nha Trang", "250.000đ", "SE3"),
        Trip("ID124", "Toa ngồi mềm", 21, "08:15", "11:00", "2h 45m", "Ga Sài Gòn", "Ga Nha Trang", "180.000đ", "SE5")
    )
    
    private fun getMockCoaches(): List<Coach> {
        val seats = (1..40).map { i ->
            val status = if (i % 5 == 0) SeatStatus.SOLD else SeatStatus.AVAILABLE
            Seat(number = "A$i", status = status, price = 250000)
        }
        return listOf(Coach("C1", "Toa 1", seats), Coach("C2", "Toa 2", seats))
    }
}