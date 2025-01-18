package com.sajad.mytrainingtracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sajad.mytrainingtracker.data.entities.Goal

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: Goal)

    @Update
    suspend fun update(goal: Goal)

    @Query("UPDATE goal SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateIsCompleted(id: Int, isCompleted: Int)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("SELECT * FROM goal WHERE userId = :userId")
    fun getByUserId(userId: Int): LiveData<List<Goal>>
}