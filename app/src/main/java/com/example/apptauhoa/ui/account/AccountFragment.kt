package com.example.apptauhoa.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.example.apptauhoa.databinding.FragmentAccountBinding
import com.example.apptauhoa.ui.admin.AdminLoginActivity

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
        binding.tvAdminLogin.setOnClickListener {
            val intent = Intent(requireActivity(), AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
