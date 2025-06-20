package com.example.gymbuddy.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: Schedule)

    @Query("SELECT * FROM schedule_table ORDER BY day ASC")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Delete
    suspend fun delete(schedule: Schedule)
}
