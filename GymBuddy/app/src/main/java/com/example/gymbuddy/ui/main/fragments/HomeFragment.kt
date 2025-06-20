package com.example.gymbuddy.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymbuddy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupCategoryRecyclerView()
        setupPopularWorkoutsRecyclerView()

        // TODO: Load actual data for progress, categories, and popular workouts
        // For now, the layout shows placeholder data.
    }

    private fun setupSpinner() {
        val weeksArray = resources.getStringArray(com.example.gymbuddy.R.array.weeks_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weeksArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWeeks.adapter = adapter
    }

    private fun setupCategoryRecyclerView() {
        // You'll need to create a CategoryAdapter and data class for categories
        // For example:
        // val categories = listOf("All", "Chest", "Back", "Arms", "Legs")
        // val categoryAdapter = CategoryAdapter(categories)
        // binding.rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // binding.rvCategories.adapter = categoryAdapter
    }

    private fun setupPopularWorkoutsRecyclerView() {
        // You'll need to create a PopularWorkoutAdapter and data class for workouts
        // For example:
        // val workouts = listOf(Workout("Shoulder Flex Stability", "Intermediate", "45 Minute", R.drawable.workout_image_1), ...)
        // val popularWorkoutAdapter = PopularWorkoutAdapter(workouts)
        // binding.rvPopularWorkouts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // binding.rvPopularWorkouts.adapter = popularWorkoutAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}