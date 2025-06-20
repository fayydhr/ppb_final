package com.example.gymbuddy.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.UserRepository
import com.example.gymbuddy.databinding.ActivityRegisterBinding
import com.example.gymbuddy.ui.login.LoginActivity
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel with factory
        val userRepository = UserRepository(GymDatabase.getDatabase(this).userDao())
        val factory = ViewModelFactory(userRepository = userRepository)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is RegisterViewModel.RegisterResult.Success -> {
                    Snackbar.make(binding.root, "Registration successful", Snackbar.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is RegisterViewModel.RegisterResult.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            viewModel.register(name, email, password, confirmPassword)
        }
    }
}
