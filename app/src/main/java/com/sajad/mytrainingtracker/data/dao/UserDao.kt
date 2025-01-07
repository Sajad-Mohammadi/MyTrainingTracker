package com.sajad.mytrainingtracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sajad.mytrainingtracker.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appUser: User)

    @Update
    suspend fun update(appUser: User)

    @Delete
    suspend fun delete(appUser: User)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("Update user SET isLoggedIn = 0 WHERE id = :id")
    suspend fun logout(id: Int)

    @Query("Update user SET isLoggedIn = 1 WHERE id = :id")
    suspend fun setLoggedIn(id: Int)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Int): LiveData<User>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun login(email: String, password: String): LiveData<User>

    @Query("SELECT * FROM user WHERE isLoggedIn = 1")
    fun getLoggedInUser(): LiveData<User>
}

