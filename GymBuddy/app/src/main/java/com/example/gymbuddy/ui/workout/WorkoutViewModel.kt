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

    // Perbarui tanda tangan fungsi untuk menerima field baru
    fun addWorkout(
        userId: Int,
        exerciseName: String,
        workoutType: String,
        scheduleDay: String,
        time: String,
        progress: String?, // Sekarang ini adalah String
        notes: String? = null // Tetap opsional
    ) {
        viewModelScope.launch {
            // Validasi input
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
                progress = progress, // Gunakan field progress
                notes = notes
            )
            workoutRepository.insertWorkout(workout)
            _operationResult.postValue(OperationResult.Success)
            loadWorkouts(userId) // Muat ulang workout setelah penambahan
        }
    }

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workoutId)
            _operationResult.postValue(OperationResult.Success)
            // Muat ulang workout untuk ID pengguna yang relevan setelah penghapusan
            // Anda perlu memastikan userId tersedia di ViewModel atau dicari dari data yang ada
            _workouts.value?.let { workouts ->
                loadWorkouts(workouts.firstOrNull()?.userId ?: -1)
            }
        }
    }
}