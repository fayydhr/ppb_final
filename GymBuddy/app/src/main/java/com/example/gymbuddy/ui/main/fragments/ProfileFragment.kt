// fayydhr/ppb_final/ppb_final-04d3009cc54b382f7d143063b28dbba9ab4b4681/GymBuddy/app/src/main/java/com/example/gymbuddy/ui/main/fragments/ProfileFragment.kt
package com.example.gymbuddy.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gymbuddy.databinding.FragmentProfileBinding
import com.example.gymbuddy.ui.calori.CalorieCalculatorActivity
import com.example.gymbuddy.ui.statistics.StatisticsActivity // Import StatisticsActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = arguments?.getInt("USER_ID", -1) ?: -1

        binding.btnCalorieCalculator.setOnClickListener {
            val intent = Intent(activity, CalorieCalculatorActivity::class.java)
            startActivity(intent)
        }

        // Corrected line for btn_view_statistics
        binding.btnViewStatistics.setOnClickListener {
            val intent = Intent(activity, StatisticsActivity::class.java)
            intent.putExtra("USER_ID", currentUserId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}