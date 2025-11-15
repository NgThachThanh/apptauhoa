package com.example.apptauhoa.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.google.android.material.appbar.MaterialToolbar

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the toolbar in the layout
        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)

        // Set up the navigation icon (back button)
        toolbar.setNavigationOnClickListener {
            // Navigate back to the previous screen
            findNavController().popBackStack()
        }
    }
}