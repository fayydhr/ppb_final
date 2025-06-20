package com.example.gymbuddy.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.data.repository.UserRepository
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.ui.login.LoginViewModel
import com.example.gymbuddy.ui.register.RegisterViewModel
import com.example.gymbuddy.ui.workout.WorkoutViewModel

class ViewModelFactory(
    private val userRepository: UserRepository? = null,
    private val workoutRepository: WorkoutRepository? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository ?: throw IllegalArgumentException("UserRepository required"))
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository ?: throw IllegalArgumentException("UserRepository required"))
            }
            modelClass.isAssignableFrom(WorkoutViewModel::class.java) -> {
                WorkoutViewModel(workoutRepository ?: throw IllegalArgumentException("WorkoutRepository required"))
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}
