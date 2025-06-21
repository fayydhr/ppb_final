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
import androidx.lifecycle.ViewModelProvider
import android.util.Log // Import Log

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scheduleRepository = ScheduleRepository(GymDatabase.getDatabase(this).scheduleDao())
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ScheduleViewModel::class.java]

        val userId = intent.getIntExtra("USER_ID", -1) // Tambahkan pengambilan userId, meskipun tidak digunakan langsung di ScheduleActivity untuk insert.
        Log.d("ScheduleActivity", "onCreate: userId = $userId") // LOG

        binding.btnAddSchedule.setOnClickListener {
            val day = binding.edtDay.text.toString().trim()
            val type = binding.edtType.text.toString().trim()
            val time = binding.edtTime.text.toString().trim()
            val notes = binding.edtNotes.text.toString().trim().takeIf { it.isNotBlank() }

            if (day.isNotEmpty() && type.isNotEmpty() && time.isNotEmpty()) {
                val schedule = Schedule(day = day, workoutType = type, time = time, notes = notes ?: "")
                Log.d("ScheduleActivity", "Attempting to add schedule: $day, $type, $time") // LOG
                viewModel.insert(schedule)
                Toast.makeText(this, "Schedule Added", Toast.LENGTH_SHORT).show()
                binding.edtDay.text.clear()
                binding.edtType.text.clear()
                binding.edtTime.text.clear()
                binding.edtNotes.text.clear()
            } else {
                val errorMessage = "Day, Workout Type, and Time fields must be filled"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Log.w("ScheduleActivity", "Validation failed. Schedule not added: $errorMessage") // LOG Warn
            }
        }

        viewModel.allSchedules.observe(this) { schedules ->
            // Observasi di sini, tapi di ActivityFragment yang menampilkan RecyclerView
        }
    }
}