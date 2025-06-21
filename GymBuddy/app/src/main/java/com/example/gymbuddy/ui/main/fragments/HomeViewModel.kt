package com.example.gymbuddy.ui.main.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

    private val _totalWorkouts = MutableLiveData<Int>()
    val totalWorkouts: LiveData<Int> = _totalWorkouts

    private val _totalCalories = MutableLiveData<Double>()
    val totalCalories: LiveData<Double> = _totalCalories

    private val _totalMinutes = MutableLiveData<Int>()
    val totalMinutes: LiveData<Int> = _totalMinutes

    fun loadHomeStats(userId: Int) {
        viewModelScope.launch {
            _totalWorkouts.postValue(workoutRepository.getTotalWorkoutsCount(userId))
            _totalCalories.postValue(workoutRepository.getTotalCaloriesBurned(userId) ?: 0.0) // Tangani null dengan nilai default
            _totalMinutes.postValue(workoutRepository.getTotalWorkoutMinutes(userId) ?: 0) // Tangani null dengan nilai default
        }
    }
}