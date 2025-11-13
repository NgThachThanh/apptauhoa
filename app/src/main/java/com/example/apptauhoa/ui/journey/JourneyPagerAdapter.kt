package com.example.apptauhoa.ui.journey

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class JourneyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance for each position
        return TripListFragment.newInstance(position)
    }
}