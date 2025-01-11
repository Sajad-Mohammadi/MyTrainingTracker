package com.sajad.mytrainingtracker.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val routineId: Int
) : Parcelable
