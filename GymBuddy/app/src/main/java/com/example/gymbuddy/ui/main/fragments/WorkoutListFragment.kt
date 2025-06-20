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
import com.example.gymbuddy.databinding.FragmentWorkoutListBinding // New binding for fragment
import com.example.gymbuddy.ui.workout.WorkoutAdapter
import com.example.gymbuddy.ui.workout.WorkoutLogActivity
import com.example.gymbuddy.ui.workout.WorkoutViewModel
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class WorkoutListFragment : Fragment() {

    private var _binding: FragmentWorkoutListBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var workoutViewModel: WorkoutViewModel
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get userId from parent activity (MainActivity) or arguments
        currentUserId = arguments?.getInt("USER_ID", -1) ?: -1
        if (currentUserId == -1) {
            Snackbar.make(binding.root, "User ID not found in fragment.", Snackbar.LENGTH_LONG).show()
            return
        }

        // Initialize ViewModel
        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(requireContext()).workoutDao())
        val workoutFactory = ViewModelFactory(workoutRepository = workoutRepository)
        workoutViewModel = ViewModelProvider(this, workoutFactory)[WorkoutViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        workoutViewModel.loadWorkouts(currentUserId)
    }

    override fun onResume() {
        super.onResume()
        // Reload workouts when returning to this fragment
        if (currentUserId != -1) {
            workoutViewModel.loadWorkouts(currentUserId)
        }
    }

    private fun setupRecyclerView() {
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
    }

    private fun setupClickListeners() {
        binding.btnAddWorkout.setOnClickListener {
            val intent = Intent(activity, WorkoutLogActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}