package com.example.gymbuddy.ui.calori

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalorieCalculatorViewModel : ViewModel() {

    private val _bmrResult = MutableLiveData<Double>()
    val bmrResult: LiveData<Double> = _bmrResult

    private val _tdeeResult = MutableLiveData<Double>()
    val tdeeResult: LiveData<Double> = _tdeeResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun calculateCalories(gender: String, age: Int, weightKg: Double, heightCm: Double, activityLevel: String) {
        if (age <= 0 || weightKg <= 0 || heightCm <= 0) {
            _errorMessage.postValue("Please enter valid positive values for Age, Weight, and Height.")
            return
        }

        val bmr: Double = when (gender) {
            "Male" -> (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5
            "Female" -> (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161
            else -> {
                _errorMessage.postValue("Invalid gender selected.")
                return
            }
        }
        _bmrResult.postValue(bmr)

        val tdeeFactor: Double = when (activityLevel) {
            "Sedentary" -> 1.2
            "Lightly Active" -> 1.375
            "Moderately Active" -> 1.55
            "Very Active" -> 1.725
            "Extremely Active" -> 1.9
            else -> {
                _errorMessage.postValue("Invalid activity level selected.")
                return
            }
        }
        val tdee = bmr * tdeeFactor
        _tdeeResult.postValue(tdee)
    }
}