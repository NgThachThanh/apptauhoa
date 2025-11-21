package com.example.apptauhoa.ui.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apptauhoa.data.DatabaseHelper
import com.example.apptauhoa.data.model.User
import com.example.apptauhoa.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var currentUserId: Int = -1
    private var currentUserRole: String = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        currentUserId = sharedPref.getInt("USER_ID", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        loadUserData()

        binding.btnSave.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        if (currentUserId == -1) {
            Toast.makeText(context, "Lỗi xác thực người dùng", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val user = dbHelper.getUserById(currentUserId)
        if (user != null) {
            binding.edtFullName.setText(user.fullName)
            binding.edtEmail.setText(user.email)
            binding.edtPhone.setText(user.phone)
            binding.edtDob.setText(user.dob)
            currentUserRole = user.role
        }
    }

    private fun saveUserData() {
        val fullName = binding.edtFullName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim() // Read-only but needed for object
        val phone = binding.edtPhone.text.toString().trim()
        val dob = binding.edtDob.text.toString().trim()

        if (fullName.isEmpty()) {
            Toast.makeText(context, "Họ tên không được để trống", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = User(
            id = currentUserId,
            email = email,
            fullName = fullName,
            role = currentUserRole,
            phone = phone,
            dob = dob
        )

        val result = dbHelper.updateUser(updatedUser)
        if (result > 0) {
            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            
            // Update Session (Name might have changed)
            val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("USER_NAME", fullName)
                apply()
            }
            
            findNavController().popBackStack()
        } else {
            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}