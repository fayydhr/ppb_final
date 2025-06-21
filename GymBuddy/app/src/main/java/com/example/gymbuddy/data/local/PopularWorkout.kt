package com.example.gymbuddy.data.local

import androidx.annotation.DrawableRes // Pastikan ini diimpor

data class PopularWorkout(
    val title: String,
    val difficulty: String,
    val duration: String,
    @DrawableRes val imageResId: Int // Resource ID untuk gambar workout
)