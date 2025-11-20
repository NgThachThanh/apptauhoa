package com.example.apptauhoa.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apptauhoa.databinding.ActivityManagePromotionsBinding

class ManagePromotionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagePromotionsBinding
    private lateinit var promotionAdapter: PromotionAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagePromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        loadDummyData()

        binding.fabAddPromotion.setOnClickListener {
            // In a real app, you would open a new activity or a dialog to add a promotion.
            Toast.makeText(this, "Chức năng thêm mới đang được phát triển!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        promotionAdapter = PromotionAdminAdapter(
            onEditClick = { promotion ->
                // In a real app, you would open a new activity or a dialog to edit the promotion.
                Toast.makeText(this, "Sửa: ${promotion.title}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { promotion ->
                // In a real app, you would show a confirmation dialog before deleting.
                Toast.makeText(this, "Xóa: ${promotion.title}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewPromotions.apply {
            layoutManager = GridLayoutManager(this@ManagePromotionsActivity, 2)
            adapter = promotionAdapter
        }
    }

    private fun loadDummyData() {
        val dummyPromotions = listOf(
            Promotion("1", "Vé tàu Tết 2025", "TET2025", "20%", ""),
            Promotion("2", "Chào hè sôi động", "HE2024", "15%", ""),
            Promotion("3", "Giảm giá sinh viên", "SINHVIEN30", "30%", ""),
            Promotion("4", "Đi nhóm 4 người", "NHOM4", "10%", ""),
            Promotion("5", "Khám phá Đà Nẵng", "DANANG", "25%", ""),
            Promotion("6", "Về quê ăn Tết", "VEQUE", "10%", "")
        )
        promotionAdapter.submitList(dummyPromotions)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}