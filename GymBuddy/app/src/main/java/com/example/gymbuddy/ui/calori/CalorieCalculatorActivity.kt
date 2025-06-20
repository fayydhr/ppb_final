package com.example.gymbuddy.ui.calori

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.databinding.ActivityCalorieCalculatorBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class CalorieCalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalorieCalculatorBinding
    private lateinit var viewModel: CalorieCalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalorieCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CalorieCalculatorViewModel::class.java]

        setupSpinners()
        setupClickListeners()
        setupObservers()
    }

    private fun setupSpinners() {
        // Gender Spinner
        val genders = arrayOf("Male", "Female")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = genderAdapter

        // Activity Level Spinner
        val activityLevels = arrayOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extremely Active")
        val activityLevelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, activityLevels)
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerActivityLevel.adapter = activityLevelAdapter
    }

    private fun setupClickListeners() {
        binding.btnCalculate.setOnClickListener {
            calculateCalories()
        }
    }

    private fun setupObservers() {
        val decimalFormat = DecimalFormat("#.##") // Format untuk 2 angka di belakang koma

        viewModel.bmrResult.observe(this) { bmr ->
            binding.tvBmrResultLabel.visibility = View.VISIBLE
            binding.tvBmrResult.visibility = View.VISIBLE
            binding.tvBmrResult.text = "${decimalFormat.format(bmr)} kcal"
        }

        viewModel.tdeeResult.observe(this) { tdee ->
            binding.tvTdeeResultLabel.visibility = View.VISIBLE
            binding.tvTdeeResult.visibility = View.VISIBLE
            binding.tvTdeeResult.text = "${decimalFormat.format(tdee)} kcal"
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                // Sembunyikan hasil jika ada error
                binding.tvBmrResultLabel.visibility = View.GONE
                binding.tvBmrResult.visibility = View.GONE
                binding.tvTdeeResultLabel.visibility = View.GONE
                binding.tvTdeeResult.visibility = View.GONE
            }
        }
    }

    private fun calculateCalories() {
        val gender = binding.spinnerGender.selectedItem.toString()
        val age = binding.etAge.text.toString().toIntOrNull()
        val weight = binding.etWeight.text.toString().toDoubleOrNull()
        val height = binding.etHeight.text.toString().toDoubleOrNull()
        val activityLevel = binding.spinnerActivityLevel.selectedItem.toString()

        if (age == null || weight == null || height == null) {
            Snackbar.make(binding.root, "Please fill in all fields (Age, Weight, Height) with valid numbers.", Snackbar.LENGTH_LONG).show()
            return
        }

        viewModel.calculateCalories(gender, age, weight, height, activityLevel)
    }
}