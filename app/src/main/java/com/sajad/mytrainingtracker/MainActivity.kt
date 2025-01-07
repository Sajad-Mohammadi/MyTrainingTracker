package com.sajad.mytrainingtracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.databinding.ActivityMainBinding
import com.sajad.mytrainingtracker.repository.UserRepository
import com.sajad.mytrainingtracker.viewModel.UserViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavView.setupWithNavController(navController)

        setupUserViewModel()
    }

    private fun setupUserViewModel() {
        val userRepository = UserRepository(AppDatabase.getInstance(this))
        val userViewModelFactory = UserViewModelFactory(application, userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]
    }
}