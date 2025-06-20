package com.example.gymbuddy.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    sealed class LoginResult {
        object Success : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private var userId: Int = -1

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _loginResult.postValue(LoginResult.Error("Email or password cannot be empty"))
                return@launch
            }

            val user = userRepository.getUserByEmail(email)
            if (user == null || user.password != password) {
                _loginResult.postValue(LoginResult.Error("Invalid email or password"))
            } else {
                userId = user.id
                _loginResult.postValue(LoginResult.Success)
            }
        }
    }

    fun getUserId(): Int {
        return userId
    }
}