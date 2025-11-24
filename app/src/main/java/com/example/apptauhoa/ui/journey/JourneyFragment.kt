package com.example.apptauhoa.ui.journey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentJourneyBinding
import com.google.android.material.tabs.TabLayoutMediator

class JourneyFragment : Fragment(R.layout.fragment_journey) {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentJourneyBinding.bind(view)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 3 // Changed to 3

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> UpcomingTripsFragment()
                    1 -> PastTripsFragment()
                    else -> CancelledTripsFragment() // New Fragment
                }
            }
        }
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Sắp tới"
                1 -> "Đã đi"
                else -> "Đã hủy" // New Tab
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}