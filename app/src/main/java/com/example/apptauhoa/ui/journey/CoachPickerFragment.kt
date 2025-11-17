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

        // Initialize adapter with the simplified lambda
        coachAdapter = CoachAdapter(args.coachList.toList()) { selectedCoach ->
            // Create the action with all required arguments to fix the N/A bug
            val action = CoachPickerFragmentDirections.actionCoachPickerToSeatSelection(
                coachId = selectedCoach.coachId,
                ticketCount = args.ticketCount,
                originStation = args.originStation,      // Add this argument
                destinationStation = args.destinationStation // Add this argument
            )

            // Navigate
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