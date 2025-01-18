package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.repository.GoalRepository
import kotlinx.coroutines.launch

class GoalViewModel(app: Application, private val goalRepository: GoalRepository) :
    AndroidViewModel(app) {

    fun insertGoal(goal: Goal) = viewModelScope.launch {
        goalRepository.insert(goal)
    }

    fun updateGoal(goal: Goal) = viewModelScope.launch {
        goalRepository.update(goal)
    }

    fun updateIsCompleted(id: Int, isCompleted: Int) = viewModelScope.launch {
        goalRepository.updateIsCompleted(id, isCompleted)
    }

    fun deleteGoal(goal: Goal) = viewModelScope.launch {
        goalRepository.delete(goal)
    }

    fun getGoalsByUserId(userId: Int): LiveData<List<Goal>> {
        return goalRepository.getByUserId(userId)
    }
}