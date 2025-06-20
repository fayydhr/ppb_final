package com.example.gymbuddy.ui.workout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.databinding.ActivityWorkoutLogBinding
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class WorkoutLogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutLogBinding
    private lateinit var viewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel with factory
        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(this).workoutDao())
        val factory = ViewModelFactory(workoutRepository = workoutRepository)
        viewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is WorkoutViewModel.OperationResult.Success -> {
                    Snackbar.make(binding.root, "Workout logged successfully", Snackbar.LENGTH_SHORT).show()
                    finish()
                }
                is WorkoutViewModel.OperationResult.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSaveWorkout.setOnClickListener {
            val userId = intent.getIntExtra("USER_ID", -1)
            if (userId == -1) {
                Snackbar.make(binding.root, "Invalid user ID", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val exerciseName = binding.etExerciseName.text.toString()
            val sets = binding.etSets.text.toString().toIntOrNull() ?: 0
            val reps = binding.etReps.text.toString().toIntOrNull() ?: 0
            val weight = binding.etWeight.text.toString().toFloatOrNull() ?: 0f
            val notes = binding.etNotes.text.toString().takeIf { it.isNotBlank() }

            viewModel.addWorkout(userId, exerciseName, sets, reps, weight, notes)
        }
    }
}
