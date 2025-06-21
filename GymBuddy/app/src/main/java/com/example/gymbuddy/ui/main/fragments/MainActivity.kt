package com.example.gymbuddy.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gymbuddy.R
import com.example.gymbuddy.databinding.ActivityMainBinding
import com.example.gymbuddy.ui.main.fragments.NewsFragment
import com.example.gymbuddy.ui.main.fragments.ProfileFragment
import com.example.gymbuddy.ui.main.fragments.ActivityFragment
import com.google.android.material.snackbar.Snackbar
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.UserRepository
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.gymbuddy.ui.main.fragments.HomeFragment
import com.example.gymbuddy.ui.workout.WorkoutLogActivity
import com.example.gymbuddy.ui.schedule.ScheduleActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            finish()
            return
        }

        loadAndSetWelcomeMessage(currentUserId)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment(), currentUserId)
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }

        setupBottomNavigation()
        setupFab()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment(), currentUserId)
                    true
                }
                R.id.navigation_activity -> {
                    replaceFragment(ActivityFragment(), currentUserId)
                    true
                }
                R.id.navigation_community -> {
                    replaceFragment(NewsFragment(), currentUserId)
                    true
                }
                R.id.navigation_profile -> {
                    replaceFragment(ProfileFragment(), currentUserId)
                    true
                }
                R.id.navigation_empty_space -> {
                    false // Item ini hanya untuk spasi, tidak dapat diklik
                }
                else -> false
            }
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Add New")
                .setItems(arrayOf("Add Workout", "Add Schedule")) { dialog, which ->
                    when (which) {
                        0 -> { // Add Workout
                            val intent = Intent(this, WorkoutLogActivity::class.java)
                            intent.putExtra("USER_ID", currentUserId)
                            startActivity(intent)
                        }
                        1 -> { // Add Schedule
                            val intent = Intent(this, ScheduleActivity::class.java)
                            intent.putExtra("USER_ID", currentUserId)
                            startActivity(intent)
                        }
                    }
                    dialog.dismiss()
                }
                .show()
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
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    // Anda bisa meneruskan nama pengguna ke HomeFragment jika diperlukan
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading user for welcome message: ${e.message}")
            }
        }
    }
}