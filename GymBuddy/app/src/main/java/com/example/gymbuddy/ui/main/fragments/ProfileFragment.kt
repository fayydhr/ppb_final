// app/src/main/java/com/example/gymbuddy/ui/main/fragments/ProfileFragment.kt
package com.example.gymbuddy.ui.main.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log // Import Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.databinding.FragmentProfileBinding
import com.example.gymbuddy.ui.calori.CalorieCalculatorActivity
import com.example.gymbuddy.ui.statistics.StatisticsViewModel // Import StatisticsViewModel
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.UserRepository
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.utils.ViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter // Import PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var currentUserId: Int = -1

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var statisticsViewModel: StatisticsViewModel

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
        Log.d("ProfileFragment", "onViewCreated: currentUserId = $currentUserId")

        if (currentUserId == -1) {
            // Handle error, maybe show a message or navigate to login
            Log.e("ProfileFragment", "User ID not found. Cannot load profile or statistics.")
            return
        }

        // Initialize ProfileViewModel
        val userRepository = UserRepository(GymDatabase.getDatabase(requireContext()).userDao())
        val profileFactory = ViewModelFactory(userRepository = userRepository)
        profileViewModel = ViewModelProvider(this, profileFactory)[ProfileViewModel::class.java]

        // Initialize StatisticsViewModel
        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(requireContext()).workoutDao())
        val statisticsFactory = ViewModelFactory(workoutRepository = workoutRepository)
        statisticsViewModel = ViewModelProvider(this, statisticsFactory)[StatisticsViewModel::class.java]

        setupClickListeners() // Hanya untuk tombol kalkulator kalori
        setupCharts() // Panggil setupCharts di sini
        setupObservers() // Panggil setupObservers untuk profil dan statistik

        // Muat data profil dan statistik
        profileViewModel.loadUserProfile(currentUserId)
        statisticsViewModel.loadStatistics(currentUserId)
    }

    private fun setupClickListeners() {
        binding.btnCalorieCalculator.setOnClickListener {
            val intent = Intent(activity, CalorieCalculatorActivity::class.java)
            startActivity(intent)
        }

        // Tombol View Workout Statistics sudah dihapus dari XML, jadi listener ini tidak lagi diperlukan
        // binding.btnViewStatistics.setOnClickListener { ... }
    }

    private fun setupObservers() {
        // Observer untuk data profil
        profileViewModel.userProfile.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = user.name
                binding.tvUserEmail.text = user.email
                // Anda bisa mengatur gambar profil di sini jika ada (misal: binding.ivProfilePic.setImageResource(...))
            } else {
                binding.tvUserName.text = "Guest User"
                binding.tvUserEmail.text = "N/A"
            }
        }

        // Observers untuk data statistik (disalin dari StatisticsActivity)
        statisticsViewModel.dailyDurations.observe(viewLifecycleOwner) { dailyDurations ->
            val entries = dailyDurations.mapIndexed { index, data ->
                Entry(index.toFloat(), data.totalDuration.toFloat())
            }
            val dataSet = LineDataSet(entries, "Duration (minutes)").apply {
                color = ColorTemplate.getHoloBlue()
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 3f
                setDrawCircleHole(false)
                valueTextSize = 9f
                valueTextColor = Color.WHITE
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            val lineData = LineData(dataSet)
            binding.lineChartDuration.data = lineData
            binding.lineChartDuration.xAxis.valueFormatter = IndexAxisValueFormatter(
                dailyDurations.map { SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(it.date)) }
            )
            binding.lineChartDuration.invalidate()
            Log.d("ProfileFragment", "Line chart data updated. Items: ${dailyDurations.size}")
        }

        statisticsViewModel.weeklyCalories.observe(viewLifecycleOwner) { weeklyCalories ->
            val entries = weeklyCalories.mapIndexed { index, data ->
                BarEntry(index.toFloat(), data.totalCalories.toFloat())
            }
            val dataSet = BarDataSet(entries, "Calories Burned (kcal)").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextColor = Color.WHITE
                valueTextSize = 9f
            }

            val barData = BarData(dataSet)
            binding.barChartCalories.data = barData
            binding.barChartCalories.xAxis.valueFormatter = IndexAxisValueFormatter(
                weeklyCalories.map { it.week } // Assuming week string is formatted nicely
            )
            binding.barChartCalories.setFitBars(true)
            binding.barChartCalories.invalidate()
            Log.d("ProfileFragment", "Bar chart data updated. Items: ${weeklyCalories.size}")
        }

        statisticsViewModel.workoutTypeDistribution.observe(viewLifecycleOwner) { distribution ->
            val entries = distribution.map { data ->
                PieEntry(data.count.toFloat(), data.workoutType)
            }
            val dataSet = PieDataSet(entries, "Workout Types").apply {
                colors = ColorTemplate.PASTEL_COLORS.toList() // Or choose custom colors
                sliceSpace = 3f
                selectionShift = 5f
                valueTextColor = Color.BLACK
                valueTextSize = 14f
            }

            val pieData = PieData(dataSet)
            pieData.setValueFormatter(PercentFormatter(binding.pieChartType)) // Menggunakan PercentFormatter
            binding.pieChartType.data = pieData
            binding.pieChartType.invalidate()
            Log.d("ProfileFragment", "Pie chart data updated. Items: ${distribution.size}")
        }
    }

    private fun setupCharts() {
        // Pengaturan umum chart (disalin dari StatisticsActivity)
        setupLineChart(binding.lineChartDuration)
        setupBarChart(binding.barChartCalories)
        setupPieChart(binding.pieChartType)
    }

    private fun setupLineChart(chart: LineChart) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                textColor = Color.WHITE
                valueFormatter = IndexAxisValueFormatter()
                granularity = 1f
            }
            axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                textColor = Color.WHITE
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
            legend.apply {
                isEnabled = true
                textColor = Color.WHITE
            }
        }
    }

    private fun setupBarChart(chart: BarChart) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                textColor = Color.WHITE
                valueFormatter = IndexAxisValueFormatter()
                granularity = 1f
            }
            axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                textColor = Color.WHITE
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
            legend.apply {
                isEnabled = true
                textColor = Color.WHITE
            }
        }
    }

    private fun setupPieChart(chart: PieChart) {
        chart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            setExtraOffsets(5f, 10f, 5f, 5f)
            setDragDecelerationFrictionCoef(0.95f)
            setDrawHoleEnabled(true)
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            setHoleRadius(58f)
            setTransparentCircleRadius(61f)
            setDrawCenterText(true)
            setCenterTextSize(18f)
            setCenterTextColor(Color.WHITE)
            isRotationEnabled = true
            setHighlightPerTapEnabled(true)
            legend.apply {
                isEnabled = true
                textColor = Color.WHITE
                setWordWrapEnabled(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}