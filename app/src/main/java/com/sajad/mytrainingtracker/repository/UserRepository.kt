package com.sajad.mytrainingtracker.repository

import androidx.lifecycle.LiveData
import com.sajad.mytrainingtracker.data.dao.UserDao
import com.sajad.mytrainingtracker.data.database.AppDatabase
import com.sajad.mytrainingtracker.data.entities.User

class UserRepository(private val db: AppDatabase) {

    suspend fun register(user: User) = db.userDao().insert(user)
    suspend fun update(user: User) = db.userDao().update(user)
    suspend fun delete(user: User) = db.userDao().delete(user)
    suspend fun deleteById(id: Int) = db.userDao().deleteById(id)

    fun getById(id: Int): LiveData<User> = db.userDao().getById(id)
    fun login(email: String, password: String): LiveData<User> = db.userDao().login(email, password)
    fun getLoggedInUser(): LiveData<User> = db.userDao().getLoggedInUser()
}