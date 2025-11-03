package com.example.apptauhoa.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.R

class AccountFragment : Fragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLoginNow: Button = view.findViewById(R.id.btnLoginNow)

        btnLoginNow.setOnClickListener {
            // sang màn đăng nhập
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
