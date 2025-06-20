package com.example.gymbuddy.ui.schedule

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gymbuddy.data.local.Schedule
import com.example.gymbuddy.databinding.ActivityScheduleBinding
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.ScheduleRepository
import com.example.gymbuddy.utils.ViewModelFactory
import androidx.lifecycle.ViewModelProvider // Import for ViewModelProvider

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var viewModel: ScheduleViewModel // Change from by viewModels() to ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel with factory for ScheduleViewModel as well
        val scheduleRepository = ScheduleRepository(GymDatabase.getDatabase(this).scheduleDao())
        // ScheduleViewModel needs Application, so we need a different factory or pass context directly.
        // For simplicity, let's just initialize it directly for now, as it's an AndroidViewModel.
        // If ViewModelFactory needs to handle AndroidViewModel, it needs to be modified.
        // For now, let's use the direct AndroidViewModel constructor.
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ScheduleViewModel::class.java]


        binding.btnAddSchedule.setOnClickListener {
            val day = binding.edtDay.text.toString().trim()
            val type = binding.edtType.text.toString().trim()
            val time = binding.edtTime.text.toString().trim()
            val notes = binding.edtNotes.text.toString().trim().takeIf { it.isNotBlank() }

            if (day.isNotEmpty() && type.isNotEmpty() && time.isNotEmpty()) {
                val schedule = Schedule(day = day, workoutType = type, time = time, notes = notes ?: "")
                viewModel.insert(schedule)
                Toast.makeText(this, "Schedule Added", Toast.LENGTH_SHORT).show()
                binding.edtDay.text.clear()
                binding.edtType.text.clear()
                binding.edtTime.text.clear()
                binding.edtNotes.text.clear()
            } else {
                Toast.makeText(this, "Day, Workout Type, and Time fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.allSchedules.observe(this) { schedules ->
            // TODO: Tampilkan data ke RecyclerView kalau mau
            // If you want to display schedules here, you would set up a RecyclerView and an adapter similar to Workouts.
        }
    }
}