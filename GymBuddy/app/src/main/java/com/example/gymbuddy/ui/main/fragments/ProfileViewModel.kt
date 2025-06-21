// app/src/main/java/com/example/gymbuddy/ui/main/fragments/ProfileViewModel.kt
package com.example.gymbuddy.ui.main.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.local.User
import com.example.gymbuddy.data.repository.UserRepository
import kotlinx.coroutines.launch
import android.util.Log // Import Log

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            Log.d("ProfileViewModel", "Loading user profile for ID: $userId")
            try {
                val user = userRepository.getUserById(userId)
                _userProfile.postValue(user)
                if (user != null) {
                    Log.d("ProfileViewModel", "User profile loaded: ${user.name} (${user.email})")
                } else {
                    Log.d("ProfileViewModel", "User profile not found for ID: $userId")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading user profile: ${e.message}", e)
                _userProfile.postValue(null) // Post null on error
            }
        }
    }
}