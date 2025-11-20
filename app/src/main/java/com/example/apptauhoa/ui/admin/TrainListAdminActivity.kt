package com.example.apptauhoa.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.ActivityTrainListAdminBinding

class TrainListAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainListAdminBinding
    private lateinit var trainAdapter: AdminTrainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_train_list_admin)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        loadDummyData()
    }

    private fun setupRecyclerView() {
        trainAdapter = AdminTrainAdapter()
        binding.recyclerViewAllTrains.apply {
            layoutManager = LinearLayoutManager(this@TrainListAdminActivity)
            adapter = trainAdapter
        }
    }

    private fun loadDummyData() {
        val dummyTrains = listOf(
            Train("SE1", "Hà Nội", "Sài Gòn", "19:30, 24/12", "04:30, 26/12"),
            Train("SE2", "Sài Gòn", "Hà Nội", "19:30, 24/12", "04:30, 26/12"),
            Train("TN1", "Đà Nẵng", "Hà Nội", "08:00, 25/12", "22:00, 25/12"),
            Train("SE3", "Hà Nội", "Đà Nẵng", "10:00, 25/12", "02:00, 26/12"),
            Train("SE4", "Đà Nẵng", "Hà Nội", "12:00, 26/12", "04:00, 27/12"),
            Train("SE5", "Sài Gòn", "Nha Trang", "06:00, 25/12", "14:00, 25/12")
        )
        trainAdapter.submitList(dummyTrains)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}