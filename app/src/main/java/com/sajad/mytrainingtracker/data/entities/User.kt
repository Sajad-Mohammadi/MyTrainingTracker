package com.sajad.mytrainingtracker.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean = false,
    val preferredLanguage: String,
): Parcelable
