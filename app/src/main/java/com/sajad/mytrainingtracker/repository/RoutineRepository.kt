package com.sajad.mytrainingtracker.repository

import androidx.lifecycle.LiveData
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.data.entities.Routine

class RoutineRepository(private val db: AppDatabase) {

    suspend fun insert(routine: Routine) = db.routineDao().insert(routine)
    suspend fun update(routine: Routine) = db.routineDao().update(routine)
    suspend fun delete(routine: Routine) = db.routineDao().delete(routine)

    fun getRoutinesByTrainingProgramId(programId: Int): LiveData<List<Routine>> {
        return db.routineDao().getRoutinesByTrainingProgramId(programId)
    }
}
