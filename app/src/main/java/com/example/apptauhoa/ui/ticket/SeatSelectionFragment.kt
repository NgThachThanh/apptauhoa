package com.example.apptauhoa.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SeatSelectionFragment : Fragment() {
    
    // Args
    private var adults: Int = 0
    private var children: Int = 0
    private var coaches: List<Coach> = emptyList()

    // State
    private var selectedSeats = mutableListOf<Seat>()
    private lateinit var seatmapAdapter: SeatmapAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adults = it.getInt("adults", 0)
            children = it.getInt("children", 0)
            // Correct, type-safe way to retrieve a Parcelable ArrayList
            coaches = it.getParcelableArrayList<Coach>("coaches")?.toList() ?: emptyList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_seat_selection, container, false)
        
        setupHeader(view)
        setupSeatmap(view)
        setupFooter(view)
        setupBottomNav(view)
        
        return view
    }

    private fun setupHeader(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        
        val subtitle: TextView = view.findViewById(R.id.txt_subtitle)
        val originName = arguments?.getString("originName") ?: ""
        val destinationName = arguments?.getString("destinationName") ?: ""
        val dateString = arguments?.getString("departureDate")
        
        val dateText = dateString?.let {
            LocalDate.parse(it).format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("vi", "VN")))
        } ?: ""
        
        subtitle.text = "$originName → $destinationName • $dateText"
        
        val coachTabs: TabLayout = view.findViewById(R.id.tabs_coach)
        coaches.forEach { coach ->
            coachTabs.addTab(coachTabs.newTab().setText(coach.name))
        }
        coachTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val coach = coaches[it.position]
                    seatmapAdapter.updateSeats(coach.seats)
                    selectedSeats.clear()
                    updateFooter(view)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupSeatmap(view: View) {
        val rvSeatmap: RecyclerView = view.findViewById(R.id.rv_seatmap)
        val requiredSeats = adults + children
        val columns = 5
        
        val initialSeats = coaches.firstOrNull()?.seats?.toMutableList() ?: mutableListOf()
        seatmapAdapter = SeatmapAdapter(initialSeats) { seat, position ->
            // Use the 'seat' parameter from the lambda directly for cleaner logic
            if (seat.status == SeatStatus.SELECTED) {
                seatmapAdapter.updateSeat(position, seat.copy(status = SeatStatus.AVAILABLE))
                selectedSeats.remove(seat)
            } else if (selectedSeats.size < requiredSeats) {
                val newSeat = seat.copy(status = SeatStatus.SELECTED)
                seatmapAdapter.updateSeat(position, newSeat)
                selectedSeats.add(newSeat)
            }
            updateFooter(view)
        }
        
        rvSeatmap.adapter = seatmapAdapter
        rvSeatmap.layoutManager = GridLayoutManager(context, columns)
    }
    
    private fun setupFooter(view: View) {
        updateFooter(view) // Initial call
        
        view.findViewById<Button>(R.id.btn_confirm_seats).setOnClickListener {
             val result = SeatSelectionResult(
                selectedSeats = selectedSeats.map { SeatRef("C1", it.number, it.deck) }, // Mocked coachId
                totalPrice = selectedSeats.sumOf { it.price }
             )
             val args = bundleOf("selectionResult" to result)
             findNavController().navigate(R.id.action_seat_selection_to_ticket_detail, args)
        }
    }
    
    private fun updateFooter(view: View) {
        val requiredSeats = adults + children
        val summary: TextView = view.findViewById(R.id.txt_selected_summary)
        val totalPrice: TextView = view.findViewById(R.id.txt_total_price)
        val confirmButton: Button = view.findViewById(R.id.btn_confirm_seats)
        
        summary.text = "Đã chọn ${selectedSeats.size}/$requiredSeats chỗ"
        totalPrice.text = "${selectedSeats.sumOf { it.price }}đ"
        confirmButton.isEnabled = selectedSeats.size == requiredSeats && requiredSeats > 0
    }
    
    private fun setupBottomNav(view: View) {
        val bottomNav: BottomNavigationView = view.findViewById(R.id.seat_bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_route -> findNavController().navigate(R.id.action_seat_selection_to_route_info)
                R.id.nav_facilities -> findNavController().navigate(R.id.action_seat_selection_to_facilities)
                R.id.nav_gallery -> findNavController().navigate(R.id.action_seat_selection_to_gallery)
                R.id.nav_reviews -> findNavController().navigate(R.id.action_seat_selection_to_reviews)
                R.id.nav_policy -> findNavController().navigate(R.id.action_seat_selection_to_policy)
            }
            true
        }
    }
}