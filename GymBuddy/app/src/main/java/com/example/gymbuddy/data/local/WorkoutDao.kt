package com.example.gymbuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    suspend fun getWorkoutsByUserId(userId: Int): List<Workout>

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)
}