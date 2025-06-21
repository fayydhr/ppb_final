package com.example.gymbuddy.ui.main.fragments

import android.content.Intent
import android.os.Bundle
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
import com.example.gymbuddy.ui.workout.WorkoutLogActivity // Masih diimpor jika diperlukan untuk konteks, tapi tidak diluncurkan dari sini
import com.example.gymbuddy.ui.workout.WorkoutViewModel
import com.example.gymbuddy.ui.schedule.ScheduleAdapter
import com.example.gymbuddy.ui.schedule.ScheduleActivity // Masih diimpor jika diperlukan untuk konteks, tapi tidak diluncurkan dari sini
import com.example.gymbuddy.ui.schedule.ScheduleViewModel
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

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
        if (currentUserId == -1) {
            Snackbar.make(binding.root, "User ID not found in activity fragment.", Snackbar.LENGTH_LONG).show()
            return
        }

        // Initialize Workouts ViewModel
        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(requireContext()).workoutDao())
        val workoutFactory = ViewModelFactory(workoutRepository = workoutRepository)
        workoutViewModel = ViewModelProvider(this, workoutFactory)[WorkoutViewModel::class.java]

        // Initialize Schedules ViewModel
        val scheduleRepository = ScheduleRepository(GymDatabase.getDatabase(requireContext()).scheduleDao())
        scheduleViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ScheduleViewModel::class.java]

        setupRecyclerViews()
        setupObservers()
        // setupClickListeners() dihapus karena tombol add tidak ada lagi di sini

        workoutViewModel.loadWorkouts(currentUserId)
    }

    override fun onResume() {
        super.onResume()
        if (currentUserId != -1) {
            workoutViewModel.loadWorkouts(currentUserId)
        }
    }

    private fun setupRecyclerViews() {
        // Workouts RecyclerView
        workoutAdapter = WorkoutAdapter { workout ->
            Snackbar.make(binding.root, "Deleting workout: ${workout.exerciseName}", Snackbar.LENGTH_SHORT)
                .setAction("Confirm") {
                    workoutViewModel.deleteWorkout(workout.id)
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
                    scheduleViewModel.delete(schedule)
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
        workoutViewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            workoutAdapter.submitList(workouts)
            binding.tvNoWorkoutsMessage.visibility = if (workouts.isEmpty()) View.VISIBLE else View.GONE
        }

        workoutViewModel.operationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is WorkoutViewModel.OperationResult.Success -> {
                    // Message already handled by Snackbar Action for delete, or by WorkoutLogActivity finish for add
                }
                is WorkoutViewModel.OperationResult.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        scheduleViewModel.allSchedules.observe(viewLifecycleOwner) { schedules ->
            scheduleAdapter.submitList(schedules)
            binding.tvNoSchedulesMessage.visibility = if (schedules.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}