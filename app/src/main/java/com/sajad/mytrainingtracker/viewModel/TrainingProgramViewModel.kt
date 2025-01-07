package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.repository.TrainingProgramRepository
import kotlinx.coroutines.launch

class TrainingProgramViewModel(app: Application, private val trainingProgramRepository: TrainingProgramRepository) : AndroidViewModel(app) {

    fun insertTrainingProgram(trainingProgram: TrainingProgram) = viewModelScope.launch {
        trainingProgramRepository.insert(trainingProgram)
    }

    fun updateTrainingProgram(trainingProgram: TrainingProgram) = viewModelScope.launch {
        trainingProgramRepository.update(trainingProgram)
    }

    fun deleteTrainingProgram(trainingProgram: TrainingProgram) = viewModelScope.launch {
        trainingProgramRepository.delete(trainingProgram)
    }

    fun getTrainingProgramById(id: Int) = trainingProgramRepository.getById(id)

    fun getTrainingProgramsByUserId(userId: Int) = trainingProgramRepository.getByUserId(userId)
}