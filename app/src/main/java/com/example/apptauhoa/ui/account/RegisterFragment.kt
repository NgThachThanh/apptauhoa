package com.example.apptauhoa.ui.account   // sửa cho đúng package

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apptauhoa.R
import com.google.android.material.textfield.TextInputEditText
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var edtName: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtConfirm: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtName = view.findViewById(R.id.edtName)
        edtEmail = view.findViewById(R.id.edtEmail)
        edtPassword = view.findViewById(R.id.edtPassword)
        edtConfirm = view.findViewById(R.id.edtConfirm)
        btnRegister = view.findViewById(R.id.btnRegister)
        tvGoToLogin = view.findViewById(R.id.tvGoToLogin)

        btnRegister.setOnClickListener {
            val name = edtName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()
            val confirm = edtConfirm.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass != confirm) {
                Toast.makeText(requireContext(),
                    "Mật khẩu không khớp",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(),
                    "Đăng ký thành công (demo)",
                    Toast.LENGTH_SHORT
                ).show()
                // có thể tự chuyển về Login nếu muốn
            }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}
