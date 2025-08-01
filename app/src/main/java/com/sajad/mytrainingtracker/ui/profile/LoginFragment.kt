package com.sajad.mytrainingtracker.ui.profile

import android.os.Bundle
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
import com.sajad.mytrainingtracker.databinding.FragmentLoginBinding
import com.sajad.mytrainingtracker.viewModel.UserViewModel


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var loginView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        loginView = view

        attachUiListeners()
    }

    private fun attachUiListeners() {
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (!validateInput(binding.etEmail,getString(R.string.email_is_required), email) ||
            !validateInput(binding.etPassword, getString(R.string.password_is_required), password)) {
            return
        }


        userViewModel.login(email, password).observe(viewLifecycleOwner) { user ->
            if (user == null) {
                Toast.makeText(loginView.context,
                    getString(R.string.invalid_email_or_password), Toast.LENGTH_SHORT).show()
            } else {
                onLoginSuccess(user)
                Toast.makeText(loginView.context,
                    getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                loginView.findNavController().popBackStack(R.id.navigation_profile, false)
            }
        }
    }

    private fun onLoginSuccess(user: User) {
        userViewModel.setLoggedIn(user.id)
    }

    private fun validateInput(editText: EditText, errorMessage: String, value: String): Boolean {
        return if(value.isEmpty()) {
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