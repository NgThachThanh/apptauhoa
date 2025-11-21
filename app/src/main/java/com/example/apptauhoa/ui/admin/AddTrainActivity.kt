package com.example.apptauhoa.ui.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.Coach
import com.example.apptauhoa.data.model.Station
import com.example.apptauhoa.data.model.Trip
import com.example.apptauhoa.databinding.ActivityAddTrainBinding
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTrainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTrainBinding
    private lateinit var dbHelper: DatabaseHelper
    private var stationList: List<Station> = emptyList()
    private lateinit var coachAdapter: CoachAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_train)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)
        stationList = dbHelper.getAllStations()

        setupDatePickers()
        setupStationPickers()
        setupCoachList()

        binding.buttonSaveTrain.setOnClickListener {
            saveTrain()
        }
    }

    private fun setupCoachList() {
        coachAdapter = CoachAdminAdapter(mutableListOf()) { coach ->
            coachAdapter.removeCoach(coach)
            updateEmptyState()
        }
        binding.rvCoaches.layoutManager = LinearLayoutManager(this)
        binding.rvCoaches.adapter = coachAdapter

        binding.btnAddCoach.setOnClickListener {
            showAddCoachDialog()
        }
        updateEmptyState()
    }

    private fun updateEmptyState() {
        binding.tvNoCoaches.isVisible = coachAdapter.itemCount == 0
        binding.rvCoaches.isVisible = coachAdapter.itemCount > 0
    }

    private fun showAddCoachDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_coach, null)
        
        val edtName = dialogView.findViewById<EditText>(R.id.edtCoachName)
        val spinnerType = dialogView.findViewById<Spinner>(R.id.spinnerCoachType)
        val edtPrice = dialogView.findViewById<EditText>(R.id.edtCoachPrice)
        val edtSeats = dialogView.findViewById<EditText>(R.id.edtCoachSeats)

        val types = arrayOf("Ngồi mềm điều hòa", "Giường nằm khoang 4", "Giường nằm khoang 6", "Ngồi cứng")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        spinnerType.adapter = adapter

        // Logic fixed seats
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = types[position]
                val fixedSeats = when (selectedType) {
                    "Ngồi mềm điều hòa" -> 60
                    "Giường nằm khoang 4" -> 28
                    "Giường nằm khoang 6" -> 42
                    "Ngồi cứng" -> 80
                    else -> 0
                }
                if (fixedSeats > 0) {
                    edtSeats.setText(fixedSeats.toString())
                    edtSeats.isEnabled = false
                } else {
                    edtSeats.setText("")
                    edtSeats.isEnabled = true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        AlertDialog.Builder(this)
            .setTitle("Thêm toa tàu")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _: DialogInterface, _: Int ->
                val name = edtName.text.toString().trim()
                val type = spinnerType.selectedItem.toString()
                val price = edtPrice.text.toString().toLongOrNull()
                val seats = edtSeats.text.toString().toIntOrNull()

                if (name.isNotEmpty() && price != null && seats != null) {
                    val tempCoach = Coach(
                        id = "", 
                        name = name,
                        type = type,
                        availableSeats = seats,
                        totalSeats = seats,
                        price = price
                    )
                    coachAdapter.addCoach(tempCoach)
                    updateEmptyState()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin hợp lệ", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun setupDatePickers() {
        binding.editTextDepartureTime.setOnClickListener { showDateTimePicker(binding.editTextDepartureTime) }
        binding.editTextArrivalTime.setOnClickListener { showDateTimePicker(binding.editTextArrivalTime) }
    }

    private fun setupStationPickers() {
        binding.editTextDepartureStation.setOnClickListener { showStationPickerDialog(binding.editTextDepartureStation, "Chọn Ga đi") }
        binding.editTextArrivalStation.setOnClickListener { showStationPickerDialog(binding.editTextArrivalStation, "Chọn Ga đến") }
    }

    private fun showStationPickerDialog(editText: TextInputEditText, title: String) {
        if (stationList.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu nhà ga", Toast.LENGTH_SHORT).show()
            return
        }
        val stationNames = stationList.map { "${it.name} (${it.code})" }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(stationNames) { _, which ->
                editText.setText(stationList[which].name)
            }
            .show()
    }

    private fun showDateTimePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        val sdf = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale("vi", "VN"))
                        editText.setText(sdf.format(selectedCalendar.time))
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun saveTrain() {
        val departureStation = binding.editTextDepartureStation.text.toString().trim()
        val arrivalStation = binding.editTextArrivalStation.text.toString().trim()
        val departureTimeFull = binding.editTextDepartureTime.text.toString().trim()
        val arrivalTimeFull = binding.editTextArrivalTime.text.toString().trim()
        val trainType = binding.editTextTrainType.text.toString().trim()
        
        val coaches = coachAdapter.getCoaches()

        if (departureStation.isEmpty() || arrivalStation.isEmpty() || departureTimeFull.isEmpty() ||
            arrivalTimeFull.isEmpty() || trainType.isEmpty()
        ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin hành trình", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (coaches.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất 1 toa tàu", Toast.LENGTH_SHORT).show()
            return
        }

        if (departureStation == arrivalStation) {
            Toast.makeText(this, "Ga đi và Ga đến không được trùng nhau", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val parts = departureTimeFull.split(", ")
            val timeOnly = parts[0]
            val dateOnly = parts[1]
            
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateObj = inputFormat.parse(dateOnly)
            val tripDateDb = outputFormat.format(dateObj!!)
            
            val arrivalParts = arrivalTimeFull.split(", ")
            val arrTimeOnly = arrivalParts[0]
            
            val fullSdf = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault())
            val depDate = fullSdf.parse(departureTimeFull)
            val arrDate = fullSdf.parse(arrivalTimeFull)
            
            if (arrDate!!.before(depDate)) {
                Toast.makeText(this, "Thời gian đến phải sau thời gian đi", Toast.LENGTH_SHORT).show()
                return
            }

            val diff = arrDate.time - depDate!!.time
            val hours = diff / (1000 * 60 * 60)
            val minutes = (diff / (1000 * 60)) % 60
            val durationStr = "${hours}h ${minutes}m"

            val tripId = "TRIP_${System.currentTimeMillis()}"
            
            val totalCapacity = coaches.sumOf { it.totalSeats }
            val minPrice = coaches.minOf { it.price }
            
            val distinctTypes = coaches.map { it.type }.distinct()
            val classTitleSummary = if (distinctTypes.size > 1) "Hỗn hợp (${distinctTypes.joinToString("/")})" else distinctTypes.firstOrNull() ?: "Ghế ngồi"

            val trip = Trip(
                id = tripId,
                trainCode = trainType,
                classTitle = classTitleSummary,
                seatsLeft = totalCapacity,
                departureTime = timeOnly,
                arrivalTime = arrTimeOnly,
                duration = durationStr,
                originStation = departureStation,
                destinationStation = arrivalStation,
                price = minPrice,
                tripDate = tripDateDb
            )

            dbHelper.addTrip(trip)

            for (coach in coaches) {
                dbHelper.addCoach(
                    tripId = tripId,
                    name = coach.name,
                    type = coach.type,
                    price = coach.price,
                    totalSeats = coach.totalSeats
                )
            }

            Toast.makeText(this, "Thêm chuyến tàu thành công!", Toast.LENGTH_SHORT).show()
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi xử lý: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}