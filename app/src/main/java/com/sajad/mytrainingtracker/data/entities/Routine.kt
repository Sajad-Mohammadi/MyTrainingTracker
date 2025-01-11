package com.sajad.mytrainingtracker.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "routine",
    foreignKeys = [
        ForeignKey(
            entity = TrainingProgram::class,
            parentColumns = ["id"],
            childColumns = ["trainingProgramId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val note: String,
    val trainingProgramId: Int
) : Parcelable