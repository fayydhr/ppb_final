// app/src/main/java/com/example/gymbuddy/ui/workout/WorkoutViewModel.kt
package com.example.gymbuddy.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.local.Workout
import com.example.gymbuddy.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {
    sealed class OperationResult {
        object Success : OperationResult()
        data class Error(val message: String) : OperationResult()
    }

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> = _operationResult

    fun loadWorkouts(userId: Int) {
        viewModelScope.launch {
            val workouts = workoutRepository.getWorkoutsByUserId(userId)
            _workouts.postValue(workouts)
        }
    }

    fun addWorkout(
        userId: Int,
        exerciseName: String,
        workoutType: String,
        scheduleDay: String,
        time: String,
        progress: String?,
        notes: String? = null,
        durationMinutes: Int? = null, // NEW
        caloriesBurned: Double? = null // NEW
    ) {
        viewModelScope.launch {
            if (exerciseName.isEmpty() || workoutType.isEmpty() || scheduleDay.isEmpty() || time.isEmpty() || progress.isNullOrEmpty()) {
                _operationResult.postValue(OperationResult.Error("Please fill all required fields: Exercise Name, Workout Type, Schedule Day, Time, and Progress."))
                return@launch
            }

            val workout = Workout(
                userId = userId,
                exerciseName = exerciseName,
                workoutType = workoutType,
                scheduleDay = scheduleDay,
                time = time,
                progress = progress,
                notes = notes,
                durationMinutes = durationMinutes, // Pass the new field
                caloriesBurned = caloriesBurned // Pass the new field
            )
            workoutRepository.insertWorkout(workout)
            _operationResult.postValue(OperationResult.Success)
            loadWorkouts(userId)
        }
    }

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workoutId)
            _operationResult.postValue(OperationResult.Success)
            _workouts.value?.let { workouts ->
                loadWorkouts(workouts.firstOrNull()?.userId ?: -1)
            }
        }
    }
}