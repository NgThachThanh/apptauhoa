package com.example.apptauhoa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentPromotionDetailBinding

class PromotionDetailFragment : Fragment() {

    private var _binding: FragmentPromotionDetailBinding? = null
    private val binding get() = _binding!!

    private val args: PromotionDetailFragmentArgs by navArgs()

    // Dummy data representing promotions
    private val promotions = listOf(
        Promotion("1", "Vé tàu Tết 2025", R.drawable.hinh_8, "Nội dung chi tiết của chương trình khuyến mãi Vé tàu Tết 2025. Săn vé tàu Tết sớm để có giá tốt nhất và chọn được chỗ ngồi ưng ý.\n\nNhập khuyến mãi: TET2025"),
        Promotion("2", "Chào hè sôi động", R.drawable.hinh_9, "Nội dung chi tiết của chương trình khuyến mãi Chào hè sôi động. Tận hưởng các chuyến đi với giá ưu đãi chưa từng có. Đừng bỏ lỡ!\n\nNhập khuyến mãi: HE2024"),
        Promotion("3", "Giảm giá sinh viên", R.drawable.hinh_10, "Chương trình giảm giá đặc biệt dành cho sinh viên. Vui lòng xuất trình thẻ sinh viên khi mua vé.\n\nNhập khuyến mãi: SINHVIEN30"),
        Promotion("4", "Đi nhóm 4 người", R.drawable.hinh_11, "Đi càng đông, giá càng rẻ. Giảm giá cho nhóm 4 người trở lên.\n\nNhập khuyến mãi: NHOM4")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromotionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Find the promotion by the passed ID and display it
        val promotion = promotions.find { it.id == args.promotionId }
        if (promotion != null) {
            binding.collapsingToolbar.title = promotion.title
            binding.textPromotionDetailTitle.text = promotion.title
            binding.textPromotionDetailDescription.text = promotion.description
            binding.imagePromotionDetailBanner.setImageResource(promotion.imageResId)
        } else {
            // Handle case where promotion is not found, e.g., show an error or default content
            binding.collapsingToolbar.title = "Không tìm thấy khuyến mãi"
            binding.textPromotionDetailTitle.text = "Lỗi"
            binding.textPromotionDetailDescription.text = "Không thể tìm thấy thông tin cho khuyến mãi này."
        }

        binding.buttonApplyAndBook.setOnClickListener {
            // Navigate back to the home screen to start booking
            findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// You will need to add a 'description' field to your Promotion data class
// For example, in a file like Promotion.kt:
// data class Promotion(val id: String, val title: String, val imageResId: Int, val description: String)