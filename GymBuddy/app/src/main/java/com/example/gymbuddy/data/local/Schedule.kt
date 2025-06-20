package com.example.gymbuddy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val day: String,
    val workoutType: String,
    val time: String,
    val notes: String
)
