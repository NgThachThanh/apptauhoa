package com.example.apptauhoa.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var trainAdapter: AdminTrainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        setupRecyclerView()
        setupClickListeners()

        loadDummyData()
    }

    private fun setupRecyclerView() {
        trainAdapter = AdminTrainAdapter()
        binding.recyclerViewTrains.apply {
            layoutManager = LinearLayoutManager(this@AdminDashboardActivity)
            adapter = trainAdapter
        }
    }

    private fun setupClickListeners() {
        binding.cardAddTrain.setOnClickListener {
            startActivity(Intent(this, AddTrainActivity::class.java))
        }

        binding.cardManagePromotions.setOnClickListener {
            startActivity(Intent(this, ManagePromotionsActivity::class.java))
        }

        binding.cardViewTrainList.setOnClickListener {
            startActivity(Intent(this, TrainListAdminActivity::class.java))
        }
    }

    private fun loadDummyData() {
        val dummyTrains = listOf(
            Train("SE1", "Hà Nội", "Sài Gòn", "19:30, 24/12", "04:30, 26/12"),
            Train("SE2", "Sài Gòn", "Hà Nội", "19:30, 24/12", "04:30, 26/12"),
            Train("TN1", "Đà Nẵng", "Hà Nội", "08:00, 25/12", "22:00, 25/12")
        )
        trainAdapter.submitList(dummyTrains)
    }
}