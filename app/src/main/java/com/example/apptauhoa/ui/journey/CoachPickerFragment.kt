package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.databinding.FragmentCoachPickerBinding

class CoachPickerFragment : Fragment() {

    private var _binding: FragmentCoachPickerBinding? = null
    private val binding get() = _binding!!

    private val args: CoachPickerFragmentArgs by navArgs()
    private lateinit var coachAdapter: CoachAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoachPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.txtRouteTitle.text = "${args.originStation} – ${args.destinationStation}"
        binding.txtRouteSubtitle.text = "${args.trainCode} • ${args.tripDate}"

        coachAdapter = CoachAdapter(args.coachList.toList()) { selectedCoach ->
            val action = CoachPickerFragmentDirections.actionCoachPickerToSeatSelection(
                coachId = selectedCoach.id,
                coachName = selectedCoach.name,
                coachType = selectedCoach.type,
                ticketCount = args.ticketCount,
                originStation = args.originStation,
                destinationStation = args.destinationStation,
                passengerCount = args.ticketCount,
                tripId = args.tripId,
                trainCode = args.trainCode,
                tripDate = args.tripDate,
                departureTime = args.departureTime,
                arrivalTime = args.arrivalTime,
                originalPrice = selectedCoach.price
            )

            findNavController().navigate(action)
        }
        
        binding.rvCoachList.layoutManager = LinearLayoutManager(context)
        binding.rvCoachList.adapter = coachAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}