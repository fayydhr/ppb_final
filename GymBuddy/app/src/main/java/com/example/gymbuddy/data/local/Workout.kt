package com.example.gymbuddy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
)