// app/src/main/java/com/example/gymbuddy/ui/statistics/StatisticsViewModel.kt
package com.example.gymbuddy.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.local.DailyDuration
import com.example.gymbuddy.data.local.WeeklyCalories
import com.example.gymbuddy.data.local.WorkoutTypeCount
import com.example.gymbuddy.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

    private val _dailyDurations = MutableLiveData<List<DailyDuration>>()
    val dailyDurations: LiveData<List<DailyDuration>> = _dailyDurations

    private val _weeklyCalories = MutableLiveData<List<WeeklyCalories>>()
    val weeklyCalories: LiveData<List<WeeklyCalories>> = _weeklyCalories

    private val _workoutTypeDistribution = MutableLiveData<List<WorkoutTypeCount>>()
    val workoutTypeDistribution: LiveData<List<WorkoutTypeCount>> = _workoutTypeDistribution

    fun loadStatistics(userId: Int) {
        viewModelScope.launch {
            _dailyDurations.postValue(workoutRepository.getDailyWorkoutDurations(userId))
            _weeklyCalories.postValue(workoutRepository.getWeeklyCaloriesBurned(userId))
            _workoutTypeDistribution.postValue(workoutRepository.getWorkoutTypeDistribution(userId))
        }
    }
}