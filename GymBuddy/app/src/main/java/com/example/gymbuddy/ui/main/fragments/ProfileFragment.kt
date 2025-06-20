package com.example.gymbuddy.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gymbuddy.databinding.FragmentProfileBinding
import com.example.gymbuddy.ui.calori.CalorieCalculatorActivity // Import CalorieCalculatorActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalorieCalculator.setOnClickListener {
            val intent = Intent(activity, CalorieCalculatorActivity::class.java)
            startActivity(intent)
        }
        // Tambahkan logika lain untuk menampilkan detail profil di sini
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}