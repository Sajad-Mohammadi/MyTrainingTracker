package com.sajad.mytrainingtracker.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "goal",
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
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val description: String,
    val priority: Int,
    val isCompleted: Int,
    val userId: Int
) : Parcelable
