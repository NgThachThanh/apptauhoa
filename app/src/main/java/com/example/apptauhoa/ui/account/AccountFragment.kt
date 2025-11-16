package com.example.apptauhoa.ui.account

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R

class AccountFragment : Fragment(R.layout.fragment_account) {

    private val TAG = "AccountFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: AccountFragment has been created.")

        // Find the views. Note that the menu items are now RelativeLayouts.
        val btnLoginNow: TextView = view.findViewById(R.id.btnLoginNow)
        val introLayout: RelativeLayout = view.findViewById(R.id.tvIntro)
        val routesLayout: RelativeLayout = view.findViewById(R.id.tvRoutes)
        val officeLayout: RelativeLayout = view.findViewById(R.id.tvOffice)
        val settingsLayout: RelativeLayout = view.findViewById(R.id.tvSettings)

        // --- Set OnClick Listeners ---

        btnLoginNow.setOnClickListener {
            Log.d(TAG, "CLICK: 'Login Now' button pressed")
            try {
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
            } catch (e: Exception) {
                Log.e(TAG, "ERROR: Could not navigate to loginFragment", e)
            }
        }

        introLayout.setOnClickListener {
            Log.d(TAG, "CLICK: 'Intro' menu item pressed")
            try {
                findNavController().navigate(R.id.action_accountFragment_to_introFragment)
            } catch (e: Exception) {
                Log.e(TAG, "ERROR: Could not navigate to introFragment", e)
            }
        }

        routesLayout.setOnClickListener {
            Log.d(TAG, "CLICK: 'Routes' menu item pressed")
            try {
                findNavController().navigate(R.id.action_accountFragment_to_routesFragment)
            } catch (e: Exception) {
                Log.e(TAG, "ERROR: Could not navigate to routesFragment", e)
            }
        }

        officeLayout.setOnClickListener {
            Log.d(TAG, "CLICK: 'Office' menu item pressed")
            try {
                findNavController().navigate(R.id.action_accountFragment_to_officeFragment)
            } catch (e: Exception) {
                Log.e(TAG, "ERROR: Could not navigate to officeFragment", e)
            }
        }

        settingsLayout.setOnClickListener {
            Log.d(TAG, "CLICK: 'Settings' menu item pressed")
            try {
                // Ensure you have this action in your nav_graph.xml
                findNavController().navigate(R.id.action_accountFragment_to_settingsFragment)
            } catch (e: Exception) {
                Log.e(TAG, "ERROR: Could not navigate to settingsFragment", e)
            }
        }
    }
}