package com.sajad.mytrainingtracker.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sajad.mytrainingtracker.MainActivity
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.User
import com.sajad.mytrainingtracker.databinding.FragmentRegisterBinding
import com.sajad.mytrainingtracker.viewModel.UserViewModel


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var registerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        registerView = view

        attachUiListeners()
    }

    private fun attachUiListeners() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val firstname = binding.etFirstname.text.toString().trim()
        val lastname = binding.etLastname.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (!validateInput(binding.etFirstname, getString(R.string.first_name_is_required), firstname) ||
            !validateInput(binding.etLastname, getString(R.string.last_name_is_required), lastname) ||
            !validateInput(binding.etEmail, getString(R.string.email_is_required), email) ||
            !validateInput(binding.etPassword, getString(R.string.password_is_required), password)
        ) return

        userViewModel.registerUser(
            User(
                0,
                firstname,
                lastname,
                email,
                password,
                true,
                "en"
            )
        )
        Toast.makeText(registerView.context,
            getString(R.string.user_registered_successfully), Toast.LENGTH_SHORT)
            .show()
        registerView.findNavController().popBackStack(R.id.navigation_profile, false)
    }

    private fun validateInput(editText: EditText, errorMessage: String, value: String): Boolean {
        return if (value.isEmpty()) {
            editText.error = errorMessage
            editText.requestFocus()
            false
        } else {
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}