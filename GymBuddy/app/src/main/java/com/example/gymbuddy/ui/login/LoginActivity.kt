package com.example.gymbuddy.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.UserRepository
import com.example.gymbuddy.databinding.ActivityLoginBinding
import com.example.gymbuddy.ui.dashboard.DashboardActivity
import com.example.gymbuddy.ui.register.RegisterActivity
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel with factory
        val userRepository = UserRepository(GymDatabase.getDatabase(this).userDao())
        val factory = ViewModelFactory(userRepository = userRepository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    val userId = viewModel.getUserId()
                    if (userId != -1) {
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java).apply {
                            putExtra("USER_ID", userId)
                        })
                        finish()
                    } else {
                        Snackbar.make(binding.root, "User not found", Snackbar.LENGTH_SHORT).show()
                    }
                }
                is LoginViewModel.LoginResult.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
