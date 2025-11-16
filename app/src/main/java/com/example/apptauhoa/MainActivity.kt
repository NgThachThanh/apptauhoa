package com.example.apptauhoa

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Use the standard setupWithNavController.
        // This handles all navigation logic, including back stack, correctly.
        navView.setupWithNavController(navController)

        // Optional: Hide BottomNav on specific screens like Login/Register
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Add any destinations here where you want to hide the bottom nav
                R.id.loginFragment, R.id.registerFragment -> {
                    navView.visibility = View.GONE
                }
                else -> {
                    navView.visibility = View.VISIBLE
                }
            }
        }
    }
}
