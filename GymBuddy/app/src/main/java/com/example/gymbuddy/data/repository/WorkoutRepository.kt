// app/src/main/java/com/example/gymbuddy/data/repository/WorkoutRepository.kt
package com.example.gymbuddy.data.repository

import com.example.gymbuddy.data.local.Workout
import com.example.gymbuddy.data.local.WorkoutDao
import com.example.gymbuddy.data.local.DailyDuration
import com.example.gymbuddy.data.local.WeeklyCalories
import com.example.gymbuddy.data.local.WorkoutTypeCount


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

    // New functions for statistics
    suspend fun getDailyWorkoutDurations(userId: Int): List<DailyDuration> {
        return workoutDao.getDailyWorkoutDurations(userId)
    }

    suspend fun getWeeklyCaloriesBurned(userId: Int): List<WeeklyCalories> {
        return workoutDao.getWeeklyCaloriesBurned(userId)
    }

    suspend fun getWorkoutTypeDistribution(userId: Int): List<WorkoutTypeCount> {
        return workoutDao.getWorkoutTypeDistribution(userId)
    }

    // === NEW FUNCTIONS FOR HOME SCREEN SUMMARY ===
    suspend fun getTotalWorkoutsCount(userId: Int): Int {
        return workoutDao.getTotalWorkoutsCount(userId)
    }

    suspend fun getTotalCaloriesBurned(userId: Int): Double? {
        return workoutDao.getTotalCaloriesBurned(userId)
    }

    suspend fun getTotalWorkoutMinutes(userId: Int): Int? {
        return workoutDao.getTotalWorkoutMinutes(userId)
    }
}