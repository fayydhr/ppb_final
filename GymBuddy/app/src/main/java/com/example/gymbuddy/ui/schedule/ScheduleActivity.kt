package com.example.gymbuddy.ui.schedule

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gymbuddy.data.local.Schedule
import com.example.gymbuddy.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddSchedule.setOnClickListener {
            val day = binding.edtDay.text.toString()
            val type = binding.edtType.text.toString()
            val time = binding.edtTime.text.toString()
            val notes = binding.edtNotes.text.toString()

            if (day.isNotEmpty() && type.isNotEmpty() && time.isNotEmpty()) {
                val schedule = Schedule(day = day, workoutType = type, time = time, notes = notes)
                viewModel.insert(schedule)
                Toast.makeText(this, "Schedule Added", Toast.LENGTH_SHORT).show()
                binding.edtDay.text.clear()
                binding.edtType.text.clear()
                binding.edtTime.text.clear()
                binding.edtNotes.text.clear()
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.allSchedules.observe(this) { schedules ->
            // TODO: Tampilkan data ke RecyclerView kalau mau
        }
    }
}
