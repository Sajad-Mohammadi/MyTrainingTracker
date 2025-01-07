package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sajad.mytrainingtracker.data.entities.User
import com.sajad.mytrainingtracker.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(app: Application, private val userRepository: UserRepository) : AndroidViewModel(app) {

    fun registerUser(user: User) = viewModelScope.launch {
        userRepository.register(user)
    }

    fun loginUser(email: String, password: String) = userRepository.login(email, password)

    fun getUserById(id: Int) = userRepository.getById(id)

    fun getLoggedInUser() = userRepository.getLoggedInUser()

    fun updateUser(user: User) = viewModelScope.launch {
        userRepository.update(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        userRepository.delete(user)
    }

    fun deleteUserById(id: Int) = viewModelScope.launch {
        userRepository.deleteById(id)
    }
}