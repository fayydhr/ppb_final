package com.example.gymbuddy.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.data.repository.ScheduleRepository
import com.example.gymbuddy.databinding.FragmentActivityBinding
import com.example.gymbuddy.ui.workout.WorkoutAdapter
import com.example.gymbuddy.ui.workout.WorkoutLogActivity
import com.example.gymbuddy.ui.workout.WorkoutViewModel
import com.example.gymbuddy.ui.schedule.ScheduleAdapter
import com.example.gymbuddy.ui.schedule.ScheduleActivity
import com.example.gymbuddy.ui.schedule.ScheduleViewModel
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.example.gymbuddy.data.local.Workout // Import class Workout
import com.example.gymbuddy.data.local.Schedule // Import class Schedule
import java.util.Calendar // Import Calendar

class ActivityFragment : Fragment() {

    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = arguments?.getInt("USER_ID", -1) ?: -1
        Log.d("ActivityFragment", "onViewCreated: currentUserId = $currentUserId")
        if (currentUserId == -1) {
            Snackbar.make(binding.root, "User ID not found in activity fragment.", Snackbar.LENGTH_LONG).show()
            return
        }

        // Initialize Workouts ViewModel (masih perlu untuk ViewModelFactory, meskipun tidak akan memuat dari DB untuk sementara)
        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(requireContext()).workoutDao())
        val workoutFactory = ViewModelFactory(workoutRepository = workoutRepository)
        workoutViewModel = ViewModelProvider(this, workoutFactory)[WorkoutViewModel::class.java]

        // Initialize Schedules ViewModel
        val scheduleRepository = ScheduleRepository(GymDatabase.getDatabase(requireContext()).scheduleDao())
        scheduleViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ScheduleViewModel::class.java]

        setupRecyclerViews()
        setupObservers() // Pertahankan observer, karena kita akan menguji dengan data dummy
        // setupClickListeners() dihapus karena tombol add tidak ada lagi di sini

        // =========================================================
        // START MODIFIKASI: Tambahkan data dummy di sini
        // =========================================================

        val dummyWorkouts = listOf(
            Workout(
                id = 1,
                userId = currentUserId,
                exerciseName = "Dummy Bench Press",
                workoutType = "Strength",
                scheduleDay = "Monday",
                time = "08:00 AM",
                progress = "3 sets x 10 reps @ 70kg",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -2) }.timeInMillis,
                notes = "Dummy workout for testing display."
            ),
            Workout(
                id = 2,
                userId = currentUserId,
                exerciseName = "Dummy Running",
                workoutType = "Cardio",
                scheduleDay = "Wednesday",
                time = "06:30 AM",
                progress = "5 km in 30 minutes",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis,
                durationMinutes = 30,
                caloriesBurned = 300.0,
                notes = "Fast pace, morning run."
            )
        )
        workoutAdapter.submitList(dummyWorkouts)
        Log.d("ActivityFragment", "Dummy Workouts submitted: ${dummyWorkouts.size} items") // LOG

        val dummySchedules = listOf(
            Schedule(
                id = 1,
                day = "Dummy Tuesday",
                workoutType = "Full Body",
                time = "07:00 PM",
                notes = "Dummy schedule for evening workout."
            ),
            Schedule(
                id = 2,
                day = "Dummy Friday",
                workoutType = "Leg Day",
                time = "05:00 PM",
                notes = "Dummy schedule for leg workout."
            )
        )
        scheduleAdapter.submitList(dummySchedules)
        Log.d("ActivityFragment", "Dummy Schedules submitted: ${dummySchedules.size} items") // LOG

        // =========================================================
        // AKHIR MODIFIKASI: Data dummy ditambahkan
        // =========================================================

        // **KOMENTARI ATAU HAPUS BARIS DI BAWAH INI SEMENTARA**
        // workoutViewModel.loadWorkouts(currentUserId) // Ini memuat dari database, kita ingin data dummy untuk pengujian
    }

    override fun onResume() {
        super.onResume()
        // **KOMENTARI ATAU HAPUS BARIS DI BAWAH INI SEMENTARA UNTUK PENGUJIAN DUMMY DATA**
        // Log.d("ActivityFragment", "onResume: reloading workouts for userId = $currentUserId")
        // if (currentUserId != -1) {
        //     workoutViewModel.loadWorkouts(currentUserId)
        // }
    }

    private fun setupRecyclerViews() {
        // Workouts RecyclerView
        workoutAdapter = WorkoutAdapter { workout ->
            Snackbar.make(binding.root, "Deleting workout: ${workout.exerciseName}", Snackbar.LENGTH_SHORT)
                .setAction("Confirm") {
                    // Karena ini data dummy, delete tidak akan berpengaruh ke database.
                    // Jika Anda ingin fungsionalitas delete untuk dummy data, Anda perlu
                    // membuat mutable list dan memperbarui adapter secara manual.
                    Log.d("ActivityFragment", "Attempted to delete dummy workout: ${workout.exerciseName}")
                }.show()
        }
        binding.rvWorkouts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = workoutAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
        }

        // Schedules RecyclerView
        scheduleAdapter = ScheduleAdapter { schedule ->
            Snackbar.make(binding.root, "Deleting schedule for ${schedule.day}", Snackbar.LENGTH_SHORT)
                .setAction("Confirm") {
                    // Sama seperti workout, delete untuk dummy data tidak akan berpengaruh ke database.
                    Log.d("ActivityFragment", "Attempted to delete dummy schedule: ${schedule.day}")
                }.show()
        }
        binding.rvSchedules.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupObservers() {
        // Observers ini akan tetap berfungsi, menampilkan log untuk data dummy atau data asli jika diaktifkan kembali
        workoutViewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            Log.d("ActivityFragment", "Workouts observed (from ViewModel): ${workouts.size} items for userId $currentUserId")
            workouts.forEach { Log.d("ActivityFragment", "Workout item from ViewModel: ${it.exerciseName} - ${it.userId}") }
            // workoutAdapter.submitList(workouts) // Jangan panggil ini jika Anda ingin melihat dummy data
            binding.tvNoWorkoutsMessage.visibility = if (workouts.isEmpty()) View.VISIBLE else View.GONE
        }

        workoutViewModel.operationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is WorkoutViewModel.OperationResult.Success -> {
                    Log.d("ActivityFragment", "Workout operation successful (from ViewModel)")
                }
                is WorkoutViewModel.OperationResult.Error -> {
                    Log.e("ActivityFragment", "Workout operation error (from ViewModel): ${result.message}")
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        scheduleViewModel.allSchedules.observe(viewLifecycleOwner) { schedules ->
            Log.d("ActivityFragment", "Schedules observed (from ViewModel): ${schedules.size} items")
            schedules.forEach { Log.d("ActivityFragment", "Schedule item from ViewModel: ${it.day} - ${it.workoutType}") }
            // scheduleAdapter.submitList(schedules) // Jangan panggil ini jika Anda ingin melihat dummy data
            binding.tvNoSchedulesMessage.visibility = if (schedules.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}