package com.example.gymbuddy.data.repository

import com.example.gymbuddy.data.local.Workout
import com.example.gymbuddy.data.local.WorkoutDao

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    suspend fun insertWorkout(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun getWorkoutsByUserId(userId: Int): List<Workout> {
        return workoutDao.getWorkoutsByUserId(userId)
    }

    suspend fun deleteWorkout(workoutId: Int) {
        workoutDao.deleteWorkout(workoutId)
    }
}