package com.example.apptauhoa.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.apptauhoa.MainActivity
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.ActivityAddTrainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AddTrainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTrainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_train)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonSaveTrain.setOnClickListener {
            saveTrain()
        }
    }

    private fun saveTrain() {
        val departureStation = binding.editTextDepartureStation.text.toString().trim()
        val arrivalStation = binding.editTextArrivalStation.text.toString().trim()
        val departureTimeStr = binding.editTextDepartureTime.text.toString().trim()
        val arrivalTimeStr = binding.editTextArrivalTime.text.toString().trim()
        val trainType = binding.editTextTrainType.text.toString().trim()
        val ticketPriceStr = binding.editTextTicketPrice.text.toString().trim()
        val seatCountStr = binding.editTextSeatCount.text.toString().trim()

        if (departureStation.isEmpty() || arrivalStation.isEmpty() || departureTimeStr.isEmpty() ||
            arrivalTimeStr.isEmpty() || trainType.isEmpty() || ticketPriceStr.isEmpty() || seatCountStr.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = ticketPriceStr.toDoubleOrNull()
        val seats = seatCountStr.toIntOrNull()

        if (price == null || seats == null) {
            Toast.makeText(this, "Invalid price or seat count", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val departureDate = sdf.parse(departureTimeStr)
        val arrivalDate = sdf.parse(arrivalTimeStr)

        if (departureDate == null || arrivalDate == null) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        // Here you would typically save the train information to your database.
        // For this example, we'll just show a success message.

        Toast.makeText(this, "Train saved successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}