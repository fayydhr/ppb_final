package com.example.gymbuddy.ui.schedule

import android.app.Application
import androidx.lifecycle.*
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.local.Schedule
import com.example.gymbuddy.data.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScheduleRepository
    val allSchedules: LiveData<List<Schedule>> //

    init {
        val scheduleDao = GymDatabase.getDatabase(application).scheduleDao()
        repository = ScheduleRepository(scheduleDao)
        allSchedules = repository.allSchedules.asLiveData() //
    }

    fun insert(schedule: Schedule) = viewModelScope.launch {
        repository.insert(schedule)
    }

    fun delete(schedule: Schedule) = viewModelScope.launch {
        repository.delete(schedule)
    }
}