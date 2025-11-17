package com.example.apptauhoa.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentTicketDetailBinding

class TicketDetailFragment : Fragment() {

    private var _binding: FragmentTicketDetailBinding? = null
    private val binding get() = _binding!!

    private val args: TicketDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBackToHome.setOnClickListener {
            // To resolve compiler ambiguity, we construct NavOptions separately.
            val navOptions = navOptions {
                popUpTo(R.id.nav_graph) { inclusive = true }
            }
            findNavController().navigate(R.id.navigation_home, null, navOptions)
        }

        binding.buttonViewTicket.setOnClickListener {
            val navOptions = navOptions {
                popUpTo(R.id.navigation_home)
            }
            findNavController().navigate(R.id.navigation_journey, null, navOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}