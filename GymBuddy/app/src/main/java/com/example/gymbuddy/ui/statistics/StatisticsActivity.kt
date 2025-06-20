// fayydhr/ppb_final/ppb_final-04d3009cc54b382f7d143063b28dbba9ab4b4681/GymBuddy/app/src/main/java/com/example/gymbuddy/ui/statistics/StatisticsActivity.kt
package com.example.gymbuddy.ui.statistics

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.databinding.ActivityStatisticsBinding
import com.example.gymbuddy.utils.ViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var viewModel: StatisticsViewModel
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getIntExtra("USER_ID", -1)
        if (currentUserId == -1) {
            // Handle error: user ID not found
            finish()
            return
        }

        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(this).workoutDao())
        val factory = ViewModelFactory(workoutRepository = workoutRepository)
        viewModel = ViewModelProvider(this, factory)[StatisticsViewModel::class.java]

        setupCharts()
        setupObservers()

        viewModel.loadStatistics(currentUserId)
    }

    private fun setupCharts() {
        // Common chart settings
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
                valueFormatter = IndexAxisValueFormatter() // Will be updated by observer
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
                valueFormatter = IndexAxisValueFormatter() // Will be updated by observer
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
            // Corrected line for highlightPerTapEnabled
            setHighlightPerTapEnabled(true) // Use the setter method
            legend.apply {
                isEnabled = true
                textColor = Color.WHITE
                setWordWrapEnabled(true)
            }
        }
    }

    private fun setupObservers() {
        viewModel.dailyDurations.observe(this) { dailyDurations ->
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
        }

        viewModel.weeklyCalories.observe(this) { weeklyCalories ->
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
        }

        viewModel.workoutTypeDistribution.observe(this) { distribution ->
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
            pieData.setValueFormatter(com.github.mikephil.charting.formatter.PercentFormatter(binding.pieChartType))
            binding.pieChartType.data = pieData
            binding.pieChartType.invalidate()
        }
    }
}