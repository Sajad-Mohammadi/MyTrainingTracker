package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sajad.mytrainingtracker.data.entities.Routine
import com.sajad.mytrainingtracker.repository.RoutineRepository
import kotlinx.coroutines.launch

class RoutineViewModel(app: Application, private val repository: RoutineRepository) : AndroidViewModel(app) {

    fun insertRoutine(routine: Routine) = viewModelScope.launch {
        repository.insert(routine)
    }

    fun updateRoutine(routine: Routine) = viewModelScope.launch {
        repository.update(routine)
    }

    fun deleteRoutine(routine: Routine) = viewModelScope.launch {
        repository.delete(routine)
    }

    fun getRoutinesByTrainingProgramId(programId: Int): LiveData<List<Routine>> {
        return repository.getRoutinesByTrainingProgramId(programId)
    }
}
