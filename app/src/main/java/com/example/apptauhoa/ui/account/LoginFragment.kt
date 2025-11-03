package com.example.apptauhoa.ui.account   // sửa cho đúng package của bạn

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R
import com.google.android.material.textfield.TextInputEditText
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnLoginGoogle: Button
    private lateinit var tvGoToRegister: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtEmail = view.findViewById(R.id.edtEmail)
        edtPassword = view.findViewById(R.id.edtPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnLoginGoogle = view.findViewById(R.id.btnLoginGoogle)
        tvGoToRegister = view.findViewById(R.id.tvGoToRegister)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Vui lòng nhập đủ email và mật khẩu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(),
                    "Đăng nhập thành công (demo)",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        btnLoginGoogle.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Đây chỉ là nút UI Google, chưa gắn SDK",
                Toast.LENGTH_SHORT
            ).show()
        }

        tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }
}
