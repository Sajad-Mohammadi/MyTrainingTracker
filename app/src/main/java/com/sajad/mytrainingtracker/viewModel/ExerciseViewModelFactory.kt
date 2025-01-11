package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sajad.mytrainingtracker.repository.ExerciseRepository

class ExerciseViewModelFactory(
    private val app: Application,
    private val exerciseRepository: ExerciseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExerciseViewModel(app, exerciseRepository) as T
    }
}