// app/src/main/java/com/example/gymbuddy/utils/ViewModelFactory.kt
package com.example.gymbuddy.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.data.repository.UserRepository
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.ui.login.LoginViewModel
import com.example.gymbuddy.ui.register.RegisterViewModel
import com.example.gymbuddy.ui.workout.WorkoutViewModel
import com.example.gymbuddy.ui.statistics.StatisticsViewModel
import com.example.gymbuddy.ui.main.fragments.HomeViewModel
import com.example.gymbuddy.ui.main.fragments.ProfileViewModel // Tambahkan ini

class ViewModelFactory(
    private val userRepository: UserRepository? = null,
    private val workoutRepository: WorkoutRepository? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository ?: throw IllegalArgumentException("UserRepository required")) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository ?: throw IllegalArgumentException("UserRepository required")) as T
            }
            modelClass.isAssignableFrom(WorkoutViewModel::class.java) -> {
                WorkoutViewModel(workoutRepository ?: throw IllegalArgumentException("WorkoutRepository required")) as T
            }
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) -> {
                StatisticsViewModel(workoutRepository ?: throw IllegalArgumentException("WorkoutRepository required")) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(workoutRepository ?: throw IllegalArgumentException("WorkoutRepository required for HomeViewModel")) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> { // Tambahkan kondisi ini
                ProfileViewModel(userRepository ?: throw IllegalArgumentException("UserRepository required for ProfileViewModel")) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}