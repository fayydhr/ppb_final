package com.example.gymbuddy.ui.workout

import android.util.Log // Import Log
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
            Log.d("WorkoutViewModel", "Loading workouts for userId: $userId") // LOG
            val workouts = workoutRepository.getWorkoutsByUserId(userId)
            Log.d("WorkoutViewModel", "Loaded ${workouts.size} workouts for userId: $userId") // LOG
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
        durationMinutes: Int? = null,
        caloriesBurned: Double? = null
    ) {
        viewModelScope.launch {
            if (exerciseName.isEmpty() || workoutType.isEmpty() || scheduleDay.isEmpty() || time.isEmpty() || progress.isNullOrEmpty()) {
                val errorMessage = "Please fill all required fields: Exercise Name, Workout Type, Schedule Day, Time, and Progress."
                _operationResult.postValue(OperationResult.Error(errorMessage))
                Log.e("WorkoutViewModel", "Add Workout Validation Error: $errorMessage") // LOG Error
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
                durationMinutes = durationMinutes,
                caloriesBurned = caloriesBurned
            )
            workoutRepository.insertWorkout(workout)
            _operationResult.postValue(OperationResult.Success)
            Log.d("WorkoutViewModel", "Workout added successfully for userId: $userId") // LOG Success
            loadWorkouts(userId) // Panggil loadWorkouts setelah menambahkan untuk memperbarui daftar
        }
    }

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workoutId)
            _operationResult.postValue(OperationResult.Success)
            Log.d("WorkoutViewModel", "Workout with ID $workoutId deleted successfully") // LOG Success
            // Perbarui daftar setelah penghapusan
            _workouts.value?.let { workouts ->
                loadWorkouts(workouts.firstOrNull()?.userId ?: -1)
            }
        }
    }
}