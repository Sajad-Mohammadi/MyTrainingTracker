package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sajad.mytrainingtracker.repository.TrainingProgramRepository

class TrainingProgramViewModelFactory(
    private val app: Application,
    private val trainingProgramRepository: TrainingProgramRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrainingProgramViewModel(app, trainingProgramRepository) as T
    }
}