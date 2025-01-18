package com.sajad.mytrainingtracker.repository

import androidx.lifecycle.LiveData
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.data.entities.Goal

class GoalRepository(private val db: AppDatabase) {

    suspend fun insert(goal: Goal) = db.goalDao().insert(goal)
    suspend fun update(goal: Goal) = db.goalDao().update(goal)
    suspend fun delete(goal: Goal) = db.goalDao().delete(goal)
    suspend fun updateIsCompleted(id: Int, isCompleted: Int) = db.goalDao().updateIsCompleted(id, isCompleted)

    fun getByUserId(userId: Int): LiveData<List<Goal>> {
        return db.goalDao().getByUserId(userId)
    }
}