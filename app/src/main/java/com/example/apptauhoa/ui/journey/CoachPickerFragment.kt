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

class CoachPickerFragment : Fragment() {

    private val args: CoachPickerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coach_picker, container, false)

        // Setup Header
        view.findViewById<TextView>(R.id.txt_subtitle).text = 
            "${args.trainCode} • ${args.departureTime} → ${args.arrivalTime}"

        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            findNavController().popBackStack()
        }

        // Setup RecyclerView
        val coachListRv = view.findViewById<RecyclerView>(R.id.rv_coach_list)
        coachListRv.layoutManager = LinearLayoutManager(context)
        coachListRv.adapter = CoachAdapter(args.coachList.toList()) { selectedCoach ->
            val action = CoachPickerFragmentDirections.actionCoachPickerToSeatSelection(
                tripId = args.tripId,
                trainCode = args.trainCode,
                departureTime = args.departureTime,
                arrivalTime = args.arrivalTime,
                coachClass = selectedCoach.coachType,
                price = selectedCoach.price,
                availableSeats = selectedCoach.availableSeats,
                originStation = args.originStation,
                destinationStation = args.destinationStation,
                tripDate = args.tripDate
            )
            findNavController().navigate(action)
        }

        return view
    }
}