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
    val workoutType: String,
    val scheduleDay: String,
    val time: String,
    val progress: String?,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val durationMinutes: Int? = null,
    val caloriesBurned: Double? = null
)