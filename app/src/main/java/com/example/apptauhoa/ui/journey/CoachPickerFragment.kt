package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptauhoa.R
import com.google.android.material.appbar.MaterialToolbar
// Import the necessary ticket models
import com.example.apptauhoa.ui.ticket.Coach as TicketCoach
import com.example.apptauhoa.ui.ticket.Seat
import com.example.apptauhoa.ui.ticket.SeatStatus

class CoachPickerFragment : Fragment() {

    private val args: CoachPickerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coach_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        view.findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        view.findViewById<TextView>(R.id.txt_route_title).text = "${args.originStation} – ${args.destinationStation}"
        view.findViewById<TextView>(R.id.txt_route_subtitle).text = "${args.trainCode} • ${args.tripDate}"

        // Setup RecyclerView
        val coachRecyclerView = view.findViewById<RecyclerView>(R.id.rv_coach_list)
        coachRecyclerView.layoutManager = LinearLayoutManager(context)

        val coachAdapter = CoachAdapter(args.coachList.toList()) { selectedJourneyCoach ->

            // MOCK DATA GENERATION: Create a list of TicketCoach objects with seats
            val ticketCoaches = args.coachList.map { journeyCoach ->
                TicketCoach(
                    id = journeyCoach.coachId,
                    name = journeyCoach.coachName,
                    seats = generateSeatsForCoach(journeyCoach)
                )
            }.toTypedArray() // Convert to Array for navigation component

            val action = CoachPickerFragmentDirections.actionCoachPickerToSeatSelection(
                tripId = args.tripId,
                trainCode = args.trainCode,
                departureTime = args.departureTime,
                arrivalTime = args.arrivalTime,
                originStation = args.originStation,
                destinationStation = args.destinationStation,
                tripDate = args.tripDate,
                coachClass = selectedJourneyCoach.coachType,
                price = selectedJourneyCoach.price,
                availableSeats = selectedJourneyCoach.availableSeats,
                // Pass the newly created array of TicketCoach
                coaches = ticketCoaches
            )
            findNavController().navigate(action)
        }
        coachRecyclerView.adapter = coachAdapter
    }

    private fun generateSeatsForCoach(coach: com.example.apptauhoa.ui.journey.Coach): List<Seat> {
        val seats = mutableListOf<Seat>()
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        val seatsPerRow = 5
        val aisleColumn = 3 // Make the 3rd column an aisle

        // Generate for 2 decks
        for (deck in 1..2) {
             for (i in rows.indices) {
                for (j in 1..seatsPerRow) {
                    val seatNumber = "${rows[i]}$j"
                    val id = "${coach.coachId}-$seatNumber-D$deck"

                    if (j == aisleColumn) {
                        // Add an empty seat for the aisle
                        seats.add(Seat(id = "$id-aisle", number = "", status = SeatStatus.AVAILABLE, deck = deck, price = 0))
                    } else {
                        val status = if (Math.random() > 0.7) SeatStatus.BOOKED else SeatStatus.AVAILABLE
                        seats.add(Seat(id = id, number = seatNumber, status = status, deck = deck, price = coach.price))
                    }
                }
            }
        }
        return seats
    }
}