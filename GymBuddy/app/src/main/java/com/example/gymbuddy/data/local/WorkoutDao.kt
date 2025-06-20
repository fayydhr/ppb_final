package com.example.gymbuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete // Tambahkan import Delete jika belum ada

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout)

    // Perbarui query untuk mengambil semua field termasuk yang baru
    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    suspend fun getWorkoutsByUserId(userId: Int): List<Workout>

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)
}