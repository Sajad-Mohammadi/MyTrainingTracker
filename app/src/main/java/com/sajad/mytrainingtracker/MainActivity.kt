package com.sajad.mytrainingtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.databinding.ActivityMainBinding
import com.sajad.mytrainingtracker.repository.TrainingProgramRepository
import com.sajad.mytrainingtracker.repository.UserRepository
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModelFactory
import com.sajad.mytrainingtracker.viewModel.UserViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var userViewModel: UserViewModel
    lateinit var trainingProgramViewModel: TrainingProgramViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRepository = UserRepository(AppDatabase.getInstance(this))
        val userViewModelFactory = UserViewModelFactory(application, userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        val trainingProgramRepository = TrainingProgramRepository(AppDatabase.getInstance(this))
        val trainingProgramViewModelFactory =
            TrainingProgramViewModelFactory(application, trainingProgramRepository)
        trainingProgramViewModel = ViewModelProvider(
            this,
            trainingProgramViewModelFactory
        )[TrainingProgramViewModel::class.java]


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavView.setupWithNavController(navController)

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.popBackStack(R.id.navigation_home, false)
                    navController.navigate(R.id.navigation_home)
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
        }

        //setupUserViewModel()
    }

    /*private fun setupUserViewModel() {
        val userRepository = UserRepository(AppDatabase.getInstance(this))
        val userViewModelFactory = UserViewModelFactory(application, userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        val trainingProgramRepository = TrainingProgramRepository(AppDatabase.getInstance(this))
        val trainingProgramViewModelFactory = TrainingProgramViewModelFactory(application, trainingProgramRepository)
        trainingProgramViewModel = ViewModelProvider(this, trainingProgramViewModelFactory)[TrainingProgramViewModel::class.java]
    }*/
}