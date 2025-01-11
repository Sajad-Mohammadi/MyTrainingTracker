package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sajad.mytrainingtracker.repository.RoutineRepository

class RoutineViewModelFactory(
    private val app: Application,
    private val routineRepository: RoutineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoutineViewModel(app, routineRepository) as T
    }
}
