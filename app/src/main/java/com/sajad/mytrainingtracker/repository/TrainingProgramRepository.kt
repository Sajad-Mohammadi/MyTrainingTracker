package com.sajad.mytrainingtracker.repository

import androidx.lifecycle.LiveData
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.data.entities.TrainingProgram

class TrainingProgramRepository(private val db: AppDatabase) {

    suspend fun insert(trainingProgram: TrainingProgram) = db.trainingProgramDao().insert(trainingProgram)
    suspend fun update(trainingProgram: TrainingProgram) = db.trainingProgramDao().update(trainingProgram)
    suspend fun delete(trainingProgram: TrainingProgram) = db.trainingProgramDao().delete(trainingProgram)

    fun getById(id: Int): LiveData<TrainingProgram> = db.trainingProgramDao().getById(id)
    fun getByUserId(userId: Int): LiveData<List<TrainingProgram>> = db.trainingProgramDao().getByUserId(userId)
}