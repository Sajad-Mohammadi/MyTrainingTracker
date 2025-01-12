package com.sajad.mytrainingtracker.repository

import androidx.lifecycle.LiveData
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.data.entities.Exercise

class ExerciseRepository(private val db: AppDatabase) {

    suspend fun insert(exercise: Exercise) = db.exerciseDao().insert(exercise)
    suspend fun update(exercise: Exercise) = db.exerciseDao().update(exercise)
    suspend fun delete(exercise: Exercise) = db.exerciseDao().delete(exercise)

    fun getExercisesByRoutineId(routineId: Int): LiveData<List<Exercise>> {
        return db.exerciseDao().getExercisesByRoutineId(routineId)
    }

    fun updateExercisePositions(exercises: List<Exercise>) {
        db.exerciseDao().updateExercisePositions(exercises)
    }
}