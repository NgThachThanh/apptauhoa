package com.example.apptauhoa.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.databinding.FragmentLoginBinding
import com.example.apptauhoa.ui.admin.AdminDashboardActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        
        binding.btnLoginGoogle.setOnClickListener {
            Toast.makeText(context, "Chức năng đăng nhập Google đang phát triển", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLogin() {
        val input = binding.edtEmail.text.toString().trim() // Can be email or username
        val password = binding.edtPassword.text.toString().trim()

        if (input.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val user = dbHelper.checkUser(input, password)

        if (user != null) {
            Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
            
            saveUserSession(user.id, user.fullName, user.email, user.role)

            if (user.role == "admin") {
                val intent = Intent(requireContext(), AdminDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        } else {
            Toast.makeText(context, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserSession(userId: Int, userName: String, email: String, role: String) {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("USER_ID", userId)
            putString("USER_NAME", userName)
            putString("USER_EMAIL", email)
            putString("USER_ROLE", role)
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}