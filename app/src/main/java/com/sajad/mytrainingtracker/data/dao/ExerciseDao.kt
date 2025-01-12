package com.sajad.mytrainingtracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sajad.mytrainingtracker.data.entities.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Update
    suspend fun update(exercise: Exercise)

    @Query("UPDATE exercise SET done = 0 WHERE routineId = :routineId")
    suspend fun resetDoneStatus(routineId: Int)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT MAX(position) FROM exercise WHERE routineId = :routineId")
    suspend fun getMaxPosition(routineId: Int): Int

    @Update
    fun updateExercisePositions(exercises: List<Exercise>)

    @Query("SELECT * FROM exercise WHERE routineId = :routineId ORDER BY position ASC")
    fun getExercisesByRoutineId(routineId: Int): LiveData<List<Exercise>>
}