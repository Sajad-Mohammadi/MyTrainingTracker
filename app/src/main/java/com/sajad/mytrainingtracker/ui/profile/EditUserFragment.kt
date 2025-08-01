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
import androidx.navigation.fragment.navArgs
import com.sajad.mytrainingtracker.MainActivity
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.User
import com.sajad.mytrainingtracker.databinding.FragmentEditUserBinding
import com.sajad.mytrainingtracker.viewModel.UserViewModel

class EditUserFragment : Fragment() {
    private var _binding: FragmentEditUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var editView: View
    private lateinit var currentUser: User

    private val args: EditUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        currentUser = args.user
        editView = view


        attacheUiListeners()
    }

    private fun attacheUiListeners() {
        binding.etFirstname.setText(currentUser.firstname)
        binding.etLastname.setText(currentUser.lastname)
        binding.etEmail.setText(currentUser.email)

        binding.btnSave.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val userId = currentUser.id
        val firstname = binding.etFirstname.text.toString().trim()
        val lastname = binding.etLastname.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = currentUser.password
        val preferredLanguage = currentUser.preferredLanguage

        if (!validateInput(binding.etFirstname,
                getString(R.string.first_name_is_required), firstname) ||
            !validateInput(binding.etLastname, getString(R.string.last_name_is_required), lastname) ||
            !validateInput(binding.etEmail, getString(R.string.email_is_required), email)
        ) {
            return
        }

        userViewModel.updateUser(User(userId, firstname, lastname, email, password, true, preferredLanguage))
        Toast.makeText(requireContext(),
            getString(R.string.user_updated_successfully), Toast.LENGTH_SHORT).show()
        editView.findNavController().popBackStack(R.id.navigation_profile, false)
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