package com.example.apptauhoa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.apptauhoa.ui.account.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Custom listener to handle back stack
        navView.setOnItemSelectedListener { item ->
            val builder = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.graph.startDestinationId, false)

            val options = builder.build()
            try {
                // Perform navigation with the custom options
                navController.navigate(item.itemId, null, options)
                true
            } catch (e: IllegalArgumentException) {
                // If the destination is not found, fall back to default behavior
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }

        // Keep the selected item in sync with the current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    navView.visibility = View.GONE
                }

                else -> {
                    navView.visibility = View.VISIBLE
                    val item = navView.menu.findItem(destination.id)
                    item?.isChecked = true
                }
            }
        }
    }
}