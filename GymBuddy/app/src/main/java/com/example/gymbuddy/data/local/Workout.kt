// app/src/main/java/com/example/gymbuddy/data/local/Workout.kt
package com.example.gymbuddy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val exerciseName: String,
    val workoutType: String, // NEW
    val scheduleDay: String, // NEW
    val time: String,        // NEW
    val progress: String?,   // Changed from sets, reps, weight
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null, // Keep notes if you still want them as optional
    val durationMinutes: Int? = null, // NEW: For workout duration
    val caloriesBurned: Double? = null // NEW: For calories burned
)