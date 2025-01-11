package com.sajad.mytrainingtracker.repository

import androidx.lifecycle.LiveData
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.data.entities.TrainingProgram

class TrainingProgramRepository(private val db: AppDatabase) {

    suspend fun insert(trainingProgram: TrainingProgram) = db.trainingProgramDao().insert(trainingProgram)
    suspend fun update(trainingProgram: TrainingProgram) = db.trainingProgramDao().update(trainingProgram)
    suspend fun delete(trainingProgram: TrainingProgram) = db.trainingProgramDao().delete(trainingProgram)
    suspend fun updateRecent(id: Int, userId: Int) = db.trainingProgramDao().updateRecent(id, userId)
    suspend fun setRecent(id: Int, userId: Int) = db.trainingProgramDao().setRecent(id, userId)

    fun getById(id: Int): LiveData<TrainingProgram> = db.trainingProgramDao().getById(id)
    fun getByUserId(userId: Int): LiveData<List<TrainingProgram>> = db.trainingProgramDao().getByUserId(userId)
}