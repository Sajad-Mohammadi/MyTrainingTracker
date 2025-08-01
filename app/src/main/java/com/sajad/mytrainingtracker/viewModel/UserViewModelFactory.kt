package com.sajad.mytrainingtracker.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sajad.mytrainingtracker.repository.UserRepository

class UserViewModelFactory (val app: Application, private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(app, userRepository) as T
    }
}