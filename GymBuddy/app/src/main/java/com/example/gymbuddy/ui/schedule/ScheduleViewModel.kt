package com.example.gymbuddy.ui.schedule

import android.app.Application
import android.util.Log // Import Log
import androidx.lifecycle.*
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.local.Schedule
import com.example.gymbuddy.data.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScheduleRepository
    val allSchedules: LiveData<List<Schedule>>

    init {
        Log.d("ScheduleViewModel", "ScheduleViewModel initialized") // LOG
        val scheduleDao = GymDatabase.getDatabase(application).scheduleDao()
        repository = ScheduleRepository(scheduleDao)
        allSchedules = repository.allSchedules.asLiveData()
    }

    fun insert(schedule: Schedule) = viewModelScope.launch {
        Log.d("ScheduleViewModel", "Inserting schedule: ${schedule.day} - ${schedule.workoutType}") // LOG
        repository.insert(schedule)
        Log.d("ScheduleViewModel", "Schedule inserted successfully") // LOG
    }

    fun delete(schedule: Schedule) = viewModelScope.launch {
        Log.d("ScheduleViewModel", "Deleting schedule: ${schedule.day}") // LOG
        repository.delete(schedule)
        Log.d("ScheduleViewModel", "Schedule deleted successfully") // LOG
    }
}