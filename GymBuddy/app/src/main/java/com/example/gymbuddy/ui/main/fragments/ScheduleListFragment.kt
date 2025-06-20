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
import com.example.gymbuddy.data.repository.ScheduleRepository
import com.example.gymbuddy.databinding.FragmentScheduleListBinding // New binding for fragment
import com.example.gymbuddy.ui.schedule.ScheduleActivity
import com.example.gymbuddy.ui.schedule.ScheduleAdapter
import com.example.gymbuddy.ui.schedule.ScheduleViewModel
import com.google.android.material.snackbar.Snackbar

class ScheduleListFragment : Fragment() {

    private var _binding: FragmentScheduleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var scheduleViewModel: ScheduleViewModel
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get userId from parent activity (MainActivity) or arguments
        currentUserId = arguments?.getInt("USER_ID", -1) ?: -1
        if (currentUserId == -1) {
            Snackbar.make(binding.root, "User ID not found in fragment.", Snackbar.LENGTH_LONG).show()
            // Handle this gracefully, maybe navigate back to login
        }

        // Initialize ViewModel
        val scheduleRepository = ScheduleRepository(GymDatabase.getDatabase(requireContext()).scheduleDao())
        scheduleViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ScheduleViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        // No explicit load needed for schedules as LiveData observes changes.
    }

    private fun setupRecyclerView() {
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
        }
    }

    private fun setupObservers() {
        scheduleViewModel.allSchedules.observe(viewLifecycleOwner) { schedules ->
            scheduleAdapter.submitList(schedules)
            binding.tvNoSchedulesMessage.visibility = if (schedules.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnAddSchedule.setOnClickListener {
            val intent = Intent(activity, ScheduleActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}