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
        
        val coachAdapter = CoachAdapter(args.coachList.toList()) { selectedCoach ->
            // FIX: Pass the correct arguments as defined in the navigation graph
            val action = CoachPickerFragmentDirections.actionCoachPickerToSeatSelection(
                // Trip details from the fragment's arguments
                tripId = args.tripId,
                trainCode = args.trainCode,
                departureTime = args.departureTime,
                arrivalTime = args.arrivalTime,
                originStation = args.originStation,
                destinationStation = args.destinationStation,
                tripDate = args.tripDate,

                // Details from the specific coach that was clicked
                coachClass = selectedCoach.coachType,
                price = selectedCoach.price,
                availableSeats = selectedCoach.availableSeats
            )
            findNavController().navigate(action)
        }
        coachRecyclerView.adapter = coachAdapter
    }
}