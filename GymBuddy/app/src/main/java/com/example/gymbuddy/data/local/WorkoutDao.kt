// app/src/main/java/com/example/gymbuddy/data/local/WorkoutDao.kt
package com.example.gymbuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    suspend fun getWorkoutsByUserId(userId: Int): List<Workout>

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)

    // New queries for statistics
    // Get total duration per day/week for a user
    @Query("SELECT date, SUM(durationMinutes) AS totalDuration FROM workouts WHERE userId = :userId AND durationMinutes IS NOT NULL GROUP BY strftime('%Y-%m-%d', date / 1000, 'unixepoch') ORDER BY date ASC")
    suspend fun getDailyWorkoutDurations(userId: Int): List<DailyDuration>

    // Get total calories burned per week for a user
    @Query("SELECT strftime('%Y-%W', date / 1000, 'unixepoch') AS week, SUM(caloriesBurned) AS totalCalories FROM workouts WHERE userId = :userId AND caloriesBurned IS NOT NULL GROUP BY week ORDER BY week ASC")
    suspend fun getWeeklyCaloriesBurned(userId: Int): List<WeeklyCalories>

    // Get count of workout types for a user
    @Query("SELECT workoutType, COUNT(*) AS count FROM workouts WHERE userId = :userId GROUP BY workoutType")
    suspend fun getWorkoutTypeDistribution(userId: Int): List<WorkoutTypeCount>
}

// Data classes to hold results of aggregated queries
data class DailyDuration(
    val date: Long,
    val totalDuration: Int
)

data class WeeklyCalories(
    val week: String,
    val totalCalories: Double
)

data class WorkoutTypeCount(
    val workoutType: String,
    val count: Int
)