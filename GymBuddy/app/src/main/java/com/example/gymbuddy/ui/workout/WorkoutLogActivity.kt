package com.example.gymbuddy.ui.workout

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.R // Pastikan R diimpor
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.databinding.ActivityWorkoutLogBinding
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class WorkoutLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutLogBinding
    private lateinit var workoutViewModel: WorkoutViewModel
    private var userId: Int = -1
    private var selectedTime: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Snackbar.make(binding.root, "User ID not found.", Snackbar.LENGTH_LONG).show()
            finish()
            return
        }

        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(this).workoutDao())
        val factory = ViewModelFactory(workoutRepository = workoutRepository)
        workoutViewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]

        setupSpinners()
        setupTimePicker()
        setupSaveButton()
    }

    private fun setupSpinners() {
        val exerciseNames = arrayOf("Bench Press", "Squat", "Deadlift", "Overhead Press", "Barbell Row", "Bicep Curl", "Tricep Extension", "Leg Press", "Lat Pulldown")
        val workoutTypes = arrayOf("Cardio", "Strength", "HIIT", "Flexibility", "Endurance", "Bodyweight")
        val scheduleDays = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        val exerciseAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, exerciseNames)
        exerciseAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spinnerExerciseName.adapter = exerciseAdapter

        val workoutTypeAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, workoutTypes)
        workoutTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spinnerWorkoutType.adapter = workoutTypeAdapter

        val scheduleDayAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, scheduleDays)
        scheduleDayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spinnerScheduleDay.adapter = scheduleDayAdapter
    }

    private fun setupTimePicker() {
        updateTimeDisplay()

        binding.btnPickTime.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                selectedTime.set(Calendar.MINUTE, minute)
                updateTimeDisplay()
            }
            TimePickerDialog(
                this,
                timeSetListener,
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun updateTimeDisplay() {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.tvSelectedTime.text = format.format(selectedTime.time)
    }

    private fun setupSaveButton() {
        binding.btnSaveWorkout.setOnClickListener {
            if (validateInputs()) {
                val exerciseName = binding.spinnerExerciseName.selectedItem.toString()
                val workoutType = binding.spinnerWorkoutType.selectedItem.toString()
                val scheduleDay = binding.spinnerScheduleDay.selectedItem.toString()
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.time)
                val progress = binding.etProgress.text.toString().trim()
                val durationMinutes = binding.etDurationMinutes.text.toString().toIntOrNull()
                val caloriesBurned = binding.etCaloriesBurned.text.toString().toDoubleOrNull()
                val notes = binding.etNotes.text.toString().trim().takeIf { it.isNotBlank() }

                workoutViewModel.addWorkout(
                    userId,
                    exerciseName,
                    workoutType,
                    scheduleDay,
                    time,
                    progress,
                    notes,
                    durationMinutes,
                    caloriesBurned
                )
                finish()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        if (binding.etProgress.text.isNullOrEmpty()) {
            binding.etProgress.error = "Progress is required"
            isValid = false
        }
        return isValid
    }
}