package com.example.apptauhoa.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentAccountBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        binding.btnLoginNow.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToLoginFragment())
        }
        binding.tvIntro.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToIntroFragment())
        }
        binding.tvRoutes.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToRoutesFragment())
        }
        binding.tvOffice.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToOfficeFragment())
        }
        binding.tvSettings.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToSettingsFragment())
        }
        
        binding.tvLogout.setOnClickListener {
            performLogout()
        }
        
        // Updated: Navigate to User Profile
        binding.tvUserInfo.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToUserProfileFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false)
        val userName = sharedPref.getString("USER_NAME", "Khách")
        val userEmail = sharedPref.getString("USER_EMAIL", "")

        if (isLoggedIn) {
            // Show User UI
            binding.layoutGuestHeader.isVisible = false
            binding.layoutUserHeader.isVisible = true
            binding.tvUserName.text = "Xin chào, $userName"
            binding.tvUserEmail.text = userEmail
            
            binding.tvUserInfo.isVisible = true 
            binding.tvLogout.isVisible = true
        } else {
            // Show Guest UI
            binding.layoutGuestHeader.isVisible = true
            binding.layoutUserHeader.isVisible = false
            
            binding.tvUserInfo.isVisible = false
            binding.tvLogout.isVisible = false
        }
    }

    private fun performLogout() {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
        checkLoginStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}