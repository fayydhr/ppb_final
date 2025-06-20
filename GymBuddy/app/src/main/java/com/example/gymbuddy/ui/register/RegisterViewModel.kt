package com.example.gymbuddy.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.local.User
import com.example.gymbuddy.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    sealed class RegisterResult {
        object Success : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                _registerResult.postValue(RegisterResult.Error("All fields are required"))
                return@launch
            }

            if (password != confirmPassword) {
                _registerResult.postValue(RegisterResult.Error("Passwords do not match"))
                return@launch
            }

            val existingUser = userRepository.getUserByEmail(email)
            if (existingUser != null) {
                _registerResult.postValue(RegisterResult.Error("Email already registered"))
                return@launch
            }

            val user = User(name = name, email = email, password = password)
            userRepository.insertUser(user)
            _registerResult.postValue(RegisterResult.Success)
        }
    }
}
