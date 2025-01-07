package com.sajad.mytrainingtracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sajad.mytrainingtracker.data.entities.TrainingProgram

@Dao
interface TrainingProgramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trainingProgram: TrainingProgram)

    @Update
    suspend fun update(trainingProgram: TrainingProgram)

    @Delete
    suspend fun delete(trainingProgram: TrainingProgram)

    @Query("SELECT * FROM training_program WHERE id = :id")
    fun getById(id: Int): LiveData<TrainingProgram>

    @Query("SELECT * FROM training_program WHERE userId = :userId")
    fun getByUserId(userId: Int): LiveData<List<TrainingProgram>>
}