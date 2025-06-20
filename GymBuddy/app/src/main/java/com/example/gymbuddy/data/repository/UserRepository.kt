package com.example.gymbuddy.data.repository

import com.example.gymbuddy.data.local.User
import com.example.gymbuddy.data.local.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}