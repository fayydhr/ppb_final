package com.example.gymbuddy.data.repository

import com.example.gymbuddy.data.local.Schedule
import com.example.gymbuddy.data.local.ScheduleDao
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    val allSchedules: Flow<List<Schedule>> = scheduleDao.getAllSchedules()

    suspend fun insert(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }

    suspend fun delete(schedule: Schedule) {
        scheduleDao.delete(schedule)
    }
}
