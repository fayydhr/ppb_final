package com.example.gymbuddy.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gymbuddy.R
import com.example.gymbuddy.databinding.ActivityMainBinding
import com.example.gymbuddy.ui.main.fragments.NewsFragment
import com.example.gymbuddy.ui.main.fragments.ProfileFragment
import com.example.gymbuddy.ui.main.fragments.ScheduleListFragment
import com.example.gymbuddy.ui.main.fragments.WorkoutListFragment
import com.google.android.material.snackbar.Snackbar
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.UserRepository
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getIntExtra("USER_ID", -1)
        if (currentUserId == -1) {
            Snackbar.make(binding.root, "User ID not passed. Please log in again.", Snackbar.LENGTH_LONG).show()
            // Optionally, navigate back to LoginActivity
            // startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Load user data for personalized welcome
        loadAndSetWelcomeMessage(currentUserId)

        // Set initial fragment (e.g., Recent Workout)
        if (savedInstanceState == null) { // Only add fragment if not restoring state
            replaceFragment(WorkoutListFragment(), currentUserId)
            binding.bottomNavigationView.selectedItemId = R.id.navigation_workouts // Set selected item
        }


        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    replaceFragment(ProfileFragment(), currentUserId)
                    true
                }
                R.id.navigation_workouts -> {
                    replaceFragment(WorkoutListFragment(), currentUserId)
                    true
                }
                R.id.navigation_schedules -> {
                    replaceFragment(ScheduleListFragment(), currentUserId)
                    true
                }
                R.id.navigation_news -> {
                    replaceFragment(NewsFragment(), currentUserId)
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, userId: Int) {
        val args = Bundle()
        args.putInt("USER_ID", userId)
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun loadAndSetWelcomeMessage(userId: Int) {
        lifecycleScope.launch {
            try {
                val userDao = GymDatabase.getDatabase(this@MainActivity).userDao()
                val userRepository = UserRepository(userDao)
                val user = userRepository.getUserById(userId) // Needs to be added to UserRepository and UserDao
                if (user != null) {
                    binding.tvWelcome.text = "Welcome, ${user.name}!"
                } else {
                    binding.tvWelcome.text = "Welcome, GymBuddy!"
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading user for welcome message: ${e.message}")
                binding.tvWelcome.text = "Welcome, GymBuddy!"
            }
        }
    }
}