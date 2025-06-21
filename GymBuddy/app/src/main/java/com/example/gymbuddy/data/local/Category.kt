package com.example.gymbuddy.data.local

data class Category(
    val name: String,
    var isSelected: Boolean = false // Properti untuk melacak status seleksi
)