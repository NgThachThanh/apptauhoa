package com.example.apptauhoa.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SeatSelectionFragment : Fragment() {

    // Args & State
    private var adults: Int = 0
    private var children: Int = 0
    private var coaches: List<Coach> = emptyList()
    private lateinit var seatmapAdapter: SeatmapAdapter
    private var selectedSeats = mutableListOf<Seat>()
    private var currentCoachIndex = 0
    private var currentDeck = 1 // The 'Seat' model uses Int (1 for lower, 2 for upper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adults = it.getInt("adults", 0)
            children = it.getInt("children", 0)
            val coachesArray = it.getParcelableArray("coaches")
            coaches = coachesArray?.mapNotNull { it as? Coach } ?: emptyList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_seat_selection, container, false)

        // Always setup the basic header
        setupHeader(view)

        val hasAnySeats = coaches.any { it.seats.isNotEmpty() }
        if (!hasAnySeats) {
            showEmptyState(view)
        } else {
            // Setup the rest of the interactive UI only if we have data
            showContent(view)
            setupSeatmap(view)
            setupCoachAndDeckControls(view)
            setupFooter(view)
            setupBottomNav(view)
            updateUiForSelectedCoach()
        }
        return view
    }

    // Sets up elements that are always visible
    private fun setupHeader(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        val subtitle: TextView = view.findViewById(R.id.txt_subtitle)
        val originName = arguments?.getString("originName") ?: ""
        val destinationName = arguments?.getString("destinationName") ?: ""
        val dateString = arguments?.getString("departureDate")
        val dateText = dateString?.let { LocalDate.parse(it).format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("vi", "VN"))) } ?: ""
        subtitle.text = "$originName → $destinationName • $dateText"
    }

    private fun showEmptyState(view: View) {
        view.findViewById<TextView>(R.id.txt_empty_state).isVisible = true
        view.findViewById<RecyclerView>(R.id.rv_seatmap).isVisible = false

        // Hide controls
        view.findViewById<View>(R.id.controls_container).isVisible = false
        view.findViewById<TabLayout>(R.id.tabs_coach).isVisible = false
        view.findViewById<View>(R.id.footer_card).isVisible = false
    }

    private fun showContent(view: View) {
        view.findViewById<TextView>(R.id.txt_empty_state).isVisible = false
        view.findViewById<RecyclerView>(R.id.rv_seatmap).isVisible = true
        view.findViewById<View>(R.id.controls_container).isVisible = true
        view.findViewById<TabLayout>(R.id.tabs_coach).isVisible = true
        view.findViewById<View>(R.id.footer_card).isVisible = true
    }

    private fun setupCoachAndDeckControls(view: View) {
        val coachTabs: TabLayout = view.findViewById(R.id.tabs_coach)
        coachTabs.clearOnTabSelectedListeners()
        coachTabs.removeAllTabs()
        coaches.forEach { coach -> coachTabs.addTab(coachTabs.newTab().setText(coach.name)) }
        coachTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null && currentCoachIndex != tab.position) {
                    currentCoachIndex = tab.position
                    selectedSeats.clear()
                    updateUiForSelectedCoach()
                    updateFooter(view)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        val deckChipGroup: ChipGroup = view.findViewById(R.id.chip_deck)
        deckChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val newDeck = if (checkedId == R.id.chip_upper) 2 else 1
            if (currentDeck != newDeck) {
                currentDeck = newDeck
                updateSeatListForDeck()
            }
        }
    }

    private fun setupSeatmap(view: View) {
        val rvSeatmap: RecyclerView = view.findViewById(R.id.rv_seatmap)
        val requiredSeats = adults + children

        seatmapAdapter = SeatmapAdapter(mutableListOf()) { seat, _ ->
            val index = seatmapAdapter.seats.indexOfFirst { it.id == seat.id }
            if (index == -1) return@SeatmapAdapter

            val currentSeatState = seatmapAdapter.seats[index]
            if (currentSeatState.status == SeatStatus.SELECTED) {
                seatmapAdapter.updateSeat(index, currentSeatState.copy(status = SeatStatus.AVAILABLE))
                selectedSeats.removeAll { it.id == currentSeatState.id }
            } else if (selectedSeats.size < requiredSeats) {
                val newSeat = currentSeatState.copy(status = SeatStatus.SELECTED)
                seatmapAdapter.updateSeat(index, newSeat)
                selectedSeats.add(newSeat)
            }
            updateFooter(view)
        }
        rvSeatmap.adapter = seatmapAdapter
        rvSeatmap.layoutManager = GridLayoutManager(context, 5)
    }

    private fun updateUiForSelectedCoach() {
        val view = this.view ?: return
        val currentCoach = coaches.getOrNull(currentCoachIndex)
        val deckChipGroup: ChipGroup = view.findViewById(R.id.chip_deck)

        if (currentCoach == null || currentCoach.seats.isEmpty()) {
            seatmapAdapter.updateSeats(emptyList())
            deckChipGroup.isVisible = false
            return
        }

        val hasLowerDeck = currentCoach.seats.any { it.deck == 1 }
        val hasUpperDeck = currentCoach.seats.any { it.deck == 2 }

        if (hasLowerDeck && hasUpperDeck) {
            deckChipGroup.isVisible = true
            if (deckChipGroup.checkedChipId != R.id.chip_lower) {
                currentDeck = 1
                deckChipGroup.check(R.id.chip_lower)
            }
        } else {
            deckChipGroup.isVisible = false
            currentDeck = if (hasUpperDeck) 2 else 1
        }
        updateSeatListForDeck()
    }

    private fun updateSeatListForDeck() {
        val currentCoach = coaches.getOrNull(currentCoachIndex) ?: return
        val seatsForDeck = currentCoach.seats.filter { it.deck == currentDeck }
        seatmapAdapter.updateSeats(seatsForDeck)
    }

    private fun setupFooter(view: View) {
        updateFooter(view)
        view.findViewById<Button>(R.id.btn_confirm_seats).setOnClickListener {
            val result = SeatSelectionResult(
                selectedSeats = selectedSeats.map { seat ->
                    val deckEnum = if (seat.deck == 2) Deck.UPPER else Deck.LOWER
                    SeatRef("C1", seat.number, deckEnum)
                },
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
        bottomNav.setOnItemSelectedListener { true }
    }
}