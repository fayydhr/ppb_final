package com.example.gymbuddy.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymbuddy.R
import com.example.gymbuddy.data.local.GymDatabase
import com.example.gymbuddy.data.repository.WorkoutRepository
import com.example.gymbuddy.databinding.FragmentHomeBinding
import com.example.gymbuddy.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.example.gymbuddy.data.local.PopularWorkout
import com.example.gymbuddy.data.local.Category // Import kelas data Category yang baru dibuat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private var currentUserId: Int = -1

    // Deklarasikan adapter untuk kategori
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var popularWorkoutAdapter: PopularWorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = arguments?.getInt("USER_ID", -1) ?: -1
        if (currentUserId == -1) {
            Snackbar.make(binding.root, "User ID not found in home fragment.", Snackbar.LENGTH_LONG).show()
            return
        }

        val workoutRepository = WorkoutRepository(GymDatabase.getDatabase(requireContext()).workoutDao())
        val factory = ViewModelFactory(workoutRepository = workoutRepository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupSpinner()
        setupCategoryRecyclerView() // Panggil metode ini untuk menyiapkan RecyclerView kategori
        setupPopularWorkoutsRecyclerView()
        setupObservers()
        loadHomeData()

        binding.tvUserName.text = "Buddy"
    }

    override fun onResume() {
        super.onResume()
        if (currentUserId != -1) {
            loadHomeData()
        }
    }

    private fun loadHomeData() {
        homeViewModel.loadHomeStats(currentUserId)
    }

    private fun setupObservers() {
        homeViewModel.totalWorkouts.observe(viewLifecycleOwner) { count ->
            binding.tvWorkoutsCount.text = count.toString()
        }
        homeViewModel.totalCalories.observe(viewLifecycleOwner) { calories ->
            binding.tvKcalCount.text = String.format("%.0f", calories)
        }
        homeViewModel.totalMinutes.observe(viewLifecycleOwner) { minutes ->
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            binding.tvMinutesCount.text = String.format("%02d:%02d", hours, remainingMinutes)
        }
    }

    private fun setupSpinner() {
        val weeksArray = resources.getStringArray(R.array.weeks_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, weeksArray)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spinnerWeeks.adapter = adapter
    }

    private fun setupCategoryRecyclerView() {
        // Inisialisasi CategoryAdapter dan tambahkan listener klik
        categoryAdapter = CategoryAdapter { selectedCategory ->
            // Ini adalah tempat di mana Anda bisa menangani kategori yang dipilih.
            // Misalnya, Anda bisa memfilter daftar workout populer berdasarkan kategori ini.
            Snackbar.make(binding.root, "Kategori terpilih: ${selectedCategory.name}", Snackbar.LENGTH_SHORT).show()

            // Contoh: Jika Anda ingin memfilter popular workouts, Anda akan memanggil fungsi filter di sini
            // popularWorkoutAdapter.filterByCategory(selectedCategory.name)
        }

        // Atur RecyclerView untuk kategori
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true) // Membantu kinerja jika item memiliki ukuran tetap
        }

        // === Data dummy untuk kategori ===
        // Ambil daftar tipe workout dari apa yang mungkin ada di WorkoutLogActivity atau definisikan sendiri
        val workoutTypes = arrayOf("Cardio", "Strength", "HIIT", "Flexibility", "Endurance", "Bodyweight") // Contoh dari dropdown workoutTypes
        val categories = mutableListOf<Category>()
        categories.add(Category("All", true)) // Kategori "All" dipilih secara default
        workoutTypes.forEach { type ->
            categories.add(Category(type))
        }
        categoryAdapter.submitList(categories) // Kirim data ke adapter
    }

    private fun setupPopularWorkoutsRecyclerView() {
        popularWorkoutAdapter = PopularWorkoutAdapter()
        binding.rvPopularWorkouts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularWorkoutAdapter
            setHasFixedSize(true)
        }

        // Data dummy untuk popular workouts (contoh yang sama dari sebelumnya)
        val dummyPopularWorkouts = listOf(
            PopularWorkout("Bench Press Master", "Advanced", "60 Minute", R.drawable.ic_default_profile),
            PopularWorkout("Full Body HIIT", "Intermediate", "30 Minute", R.drawable.ic_community),
            PopularWorkout("Yoga for Flexibility", "Beginner", "45 Minute", R.drawable.ic_home)
        )
        popularWorkoutAdapter.submitList(dummyPopularWorkouts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}