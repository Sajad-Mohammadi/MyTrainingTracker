package com.sajad.mytrainingtracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sajad.mytrainingtracker.MainActivity
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.adapter.UserAdapter
import com.sajad.mytrainingtracker.databinding.FragmentProfileBinding
import com.sajad.mytrainingtracker.viewModel.UserViewModel
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        val mainActivity = activity as? MainActivity
        val isDarkMode = mainActivity?.loadThemePreference() ?: false
        binding.themeSwitch.isChecked = isDarkMode

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            mainActivity?.toggleTheme(isChecked)
            mainActivity?.saveThemePreference(isChecked)
        }

        binding.languageSwitch.setOnClickListener {
            switchLanguage(it)
        }

        initVars()
        attacheUiListeners()
        setupRecyclerView()
    }

    private fun initVars() {
    }

    private fun attacheUiListeners() {
        binding.btnRegister.setOnClickListener {
            it.findNavController().navigate(R.id.navigation_register)
        }

        binding.tvLogin.setOnClickListener {
            it.findNavController().navigate(R.id.navigation_login)
        }
    }

    private fun updateUi(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            binding.tvLogin.visibility = View.GONE
            binding.btnRegister.visibility = View.GONE

            binding.profileRecyclerView.visibility = View.VISIBLE
        } else {
            binding.tvLogin.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.VISIBLE

            binding.profileRecyclerView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            onBtnEditClick = { user ->
                val action =
                    ProfileFragmentDirections.actionNavigationProfileToEditUserFragment(user)
                view?.findNavController()?.navigate(action)
            },
            onBtnLogoutClick = { user ->
                userViewModel.logout(user.id)
            },
            onBtnDeleteClick = { user ->
                userViewModel.deleteById(user.id)
            }
        )

        binding.profileRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        activity?.let {
            userViewModel.getLoggedInUser().observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    userAdapter.differ.submitList(listOf(user))
                    updateUi(true)
                } else {
                    userAdapter.differ.submitList(emptyList())
                    updateUi(false)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun switchLanguage(view: View) {
        val currentLocale = requireContext().resources.configuration.locales[0]
        val newLocale = if (currentLocale.language == "en") "sv" else "en"

        val locale = Locale(newLocale)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val prefs = requireActivity().getSharedPreferences("settings", AppCompatActivity.MODE_PRIVATE)
        prefs.edit().putString("app_language", newLocale).apply()

        requireActivity().recreate()
    }

}