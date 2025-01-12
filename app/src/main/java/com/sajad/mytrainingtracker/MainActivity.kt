package com.sajad.mytrainingtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.databinding.ActivityMainBinding
import com.sajad.mytrainingtracker.repository.ExerciseRepository
import com.sajad.mytrainingtracker.repository.RoutineRepository
import com.sajad.mytrainingtracker.repository.TrainingProgramRepository
import com.sajad.mytrainingtracker.repository.UserRepository
import com.sajad.mytrainingtracker.viewModel.ExerciseViewModel
import com.sajad.mytrainingtracker.viewModel.ExerciseViewModelFactory
import com.sajad.mytrainingtracker.viewModel.RoutineViewModel
import com.sajad.mytrainingtracker.viewModel.RoutineViewModelFactory
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModelFactory
import com.sajad.mytrainingtracker.viewModel.UserViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModelFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val prefs by lazy { getSharedPreferences("settings", MODE_PRIVATE) }

    lateinit var userViewModel: UserViewModel
    lateinit var trainingProgramViewModel: TrainingProgramViewModel
    lateinit var routineViewModel: RoutineViewModel
    lateinit var exerciseViewModel: ExerciseViewModel

    private fun applySavedLanguagePreference() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val language = prefs.getString("app_language", "en")
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedLanguagePreference()
        super.onCreate(savedInstanceState)
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        toggleTheme(loadThemePreference())

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

        val routineRepository = RoutineRepository(AppDatabase.getInstance(this))
        val routineViewModelFactory = RoutineViewModelFactory(application, routineRepository)
        routineViewModel = ViewModelProvider(this, routineViewModelFactory)[RoutineViewModel::class.java]

        val exerciseRepository = ExerciseRepository(AppDatabase.getInstance(this))
        val exerciseViewModelFactory = ExerciseViewModelFactory(application, exerciseRepository)
        exerciseViewModel = ViewModelProvider(this, exerciseViewModelFactory)[ExerciseViewModel::class.java]

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

    fun toggleTheme(isDarkMode: Boolean) {
        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun saveThemePreference(isDarkMode: Boolean) {
        prefs.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun loadThemePreference(): Boolean {
        return prefs.getBoolean("dark_mode", false) // Default to light mode
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