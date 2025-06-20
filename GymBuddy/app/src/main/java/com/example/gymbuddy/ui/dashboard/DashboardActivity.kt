package com.example.gymbuddy.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.databinding.ActivityDashboardBinding
import com.example.gymbuddy.ui.workout.WorkoutAdapter
import com.example.gymbuddy.ui.workout.WorkoutLogActivity
import com.example.gymbuddy.ui.workout.WorkoutViewModel
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var viewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel with factory
        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(this).workoutDao())
        val factory = ViewModelFactory(workoutRepository = workoutRepository)
        viewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Load workouts for the user
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Snackbar.make(binding.root, "Invalid user ID", Snackbar.LENGTH_LONG).show()
            finish()
            return
        }
        viewModel.loadWorkouts(userId)
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter { workout ->
            viewModel.deleteWorkout(workout.id)
        }
        binding.rvWorkouts.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = workoutAdapter
        }
    }

    private fun setupObservers() {
        viewModel.workouts.observe(this) { workouts ->
            workoutAdapter.submitList(workouts)
        }

        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is WorkoutViewModel.OperationResult.Success -> {
                    Snackbar.make(binding.root, "Workout deleted", Snackbar.LENGTH_SHORT).show()
                }
                is WorkoutViewModel.OperationResult.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        val userId = intent.getIntExtra("USER_ID", -1)

        binding.btnAddWorkout.setOnClickListener {
            val intent = Intent(this, WorkoutLogActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        binding.btnAddSchedule.setOnClickListener {
            val intent = Intent(this, com.example.gymbuddy.ui.schedule.ScheduleActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }
}
