package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sajad.mytrainingtracker.data.entities.Exercise
import com.sajad.mytrainingtracker.repository.ExerciseRepository
import kotlinx.coroutines.launch

class ExerciseViewModel(app: Application, private val exerciseRepository: ExerciseRepository) :
    AndroidViewModel(app) {

    fun insertExercise(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.insert(exercise)
    }

    fun updateExercise(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.update(exercise)
    }

    fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.delete(exercise)
    }

    fun updateExercises(exercises: List<Exercise>) = viewModelScope.launch {
        exercises.forEachIndexed { index, exercise ->
            exerciseRepository.update(exercise.copy(position = index))
        }
    }
    fun getMaxPositionAsync(routineId: Int, callback: (Int) -> Unit) = viewModelScope.launch {
        val maxPosition = exerciseRepository.getMaxPosition(routineId)
        callback(maxPosition)
    }

    fun getExercisesByRoutineId(routineId: Int) = exerciseRepository.getExercisesByRoutineId(routineId)
}