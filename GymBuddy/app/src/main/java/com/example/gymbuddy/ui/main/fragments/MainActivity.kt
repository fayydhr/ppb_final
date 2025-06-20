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
import com.example.gymbuddy.ui.main.fragments.ScheduleListFragment
import com.example.gymbuddy.ui.main.fragments.WorkoutListFragment
import com.google.android.material.snackbar.Snackbar
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.UserRepository
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.gymbuddy.ui.main.fragments.HomeFragment

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

        // Load user data for personalized welcome - adjust for HomeFragment header
        loadAndSetWelcomeMessage(currentUserId)

        // Set initial fragment to HomeFragment
        if (savedInstanceState == null) { // Only add fragment if not restoring state
            replaceFragment(HomeFragment(), currentUserId)
            binding.bottomNavigation.selectedItemId = R.id.navigation_home // Menggunakan binding.bottomNavigation
        }

        setupBottomNavigation()

        // Handle FAB click
        binding.fabAdd.setOnClickListener { // Menggunakan binding.fabAdd
            // Logika untuk tombol tambah (mungkin membuka WorkoutLogActivity atau dialog)
            val intent = Intent(this, com.example.gymbuddy.ui.workout.WorkoutLogActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item -> // Menggunakan binding.bottomNavigation
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment(), currentUserId)
                    true
                }
                R.id.navigation_activity -> {
                    // Replace with your Activity/Statistics Fragment
                    replaceFragment(WorkoutListFragment(), currentUserId) // Untuk sementara pakai WorkoutListFragment
                    true
                }
                R.id.navigation_community -> {
                    replaceFragment(NewsFragment(), currentUserId) // Untuk sementara pakai NewsFragment
                    true
                }
                R.id.navigation_profile -> {
                    replaceFragment(ProfileFragment(), currentUserId)
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
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    // Anda bisa meneruskan nama pengguna ke HomeFragment jika diperlukan
                    // val homeFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? HomeFragment
                    // homeFragment?.updateUserName(user.name) // Contoh fungsi di HomeFragment
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading user for welcome message: ${e.message}")
            }
        }
    }
}