package com.sajad.mytrainingtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sajad.mytrainingtracker.data.dao.ExerciseDao
import com.sajad.mytrainingtracker.data.dao.GoalDao
import com.sajad.mytrainingtracker.data.dao.RoutineDao
import com.sajad.mytrainingtracker.data.dao.TrainingProgramDao
import com.sajad.mytrainingtracker.data.dao.UserDao
import com.sajad.mytrainingtracker.data.entities.Exercise
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.data.entities.Routine
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.data.entities.User

@Database(
    entities = [
        User::class,
        TrainingProgram::class,
        Routine::class,
        Exercise::class,
        Goal::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun trainingProgramDao(): TrainingProgramDao
    abstract fun routineDao(): RoutineDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}