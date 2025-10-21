package com.example.apptauhoa.ui.lookup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R

class LookupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lookup, container, false)

        val lookupTicketButton: Button = view.findViewById(R.id.lookup_ticket_button)

        lookupTicketButton.setOnClickListener {
            findNavController().navigate(R.id.action_lookup_to_ticket_detail)
        }

        return view
    }
}