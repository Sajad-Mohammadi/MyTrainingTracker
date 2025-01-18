package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sajad.mytrainingtracker.repository.GoalRepository

class GoalViewModelFactory(
    private val app: Application,
    private val goalRepository: GoalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoalViewModel(app, goalRepository) as T
    }
}