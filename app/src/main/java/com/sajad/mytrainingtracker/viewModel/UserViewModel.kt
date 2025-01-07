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

    fun updateUser(user: User) = viewModelScope.launch {
        userRepository.update(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        userRepository.delete(user)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        userRepository.deleteById(id)
    }

    fun logout(id: Int) = viewModelScope.launch {
        userRepository.logout(id)
    }

    fun setLoggedIn(id: Int) = viewModelScope.launch {
        userRepository.setLoggedIn(id)
    }

    fun getById(id: Int) = userRepository.getById(id)

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun getLoggedInUser() = userRepository.getLoggedInUser()
}