package com.example.apptauhoa.ui.journey

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class JourneyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingTripsFragment()
            1 -> PastTripsFragment()
            2 -> CancelledTripsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}