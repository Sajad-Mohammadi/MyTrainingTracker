package com.sajad.mytrainingtracker.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "training_program",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class TrainingProgram(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val duration: Int,
    val userId: Int
): Parcelable
