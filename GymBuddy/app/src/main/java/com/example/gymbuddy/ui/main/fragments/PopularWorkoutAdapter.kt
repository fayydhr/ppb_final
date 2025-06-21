package com.example.gymbuddy.ui.main.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbuddy.data.local.PopularWorkout // Import kelas data PopularWorkout
import com.example.gymbuddy.databinding.ItemPopularWorkoutBinding // Binding untuk item_popular_workout.xml

class PopularWorkoutAdapter : ListAdapter<PopularWorkout, PopularWorkoutAdapter.PopularWorkoutViewHolder>(PopularWorkoutDiffCallback()) {

    class PopularWorkoutViewHolder(private val binding: ItemPopularWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: PopularWorkout) {
            binding.ivWorkoutImage.setImageResource(workout.imageResId) // Set gambar
            binding.tvDifficulty.text = workout.difficulty // Set kesulitan
            binding.tvWorkoutTitle.text = workout.title // Set judul workout
            binding.tvDuration.text = workout.duration // Set durasi
            // Icon hati (iv_heart) sudah ada di layout, tidak perlu logika khusus kecuali jika interaktif
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularWorkoutViewHolder {
        val binding = ItemPopularWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularWorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularWorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PopularWorkoutDiffCallback : DiffUtil.ItemCallback<PopularWorkout>() {
    override fun areItemsTheSame(oldItem: PopularWorkout, newItem: PopularWorkout): Boolean {
        // Menggunakan judul sebagai ID unik sementara, idealnya pakai ID unik dari database
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: PopularWorkout, newItem: PopularWorkout): Boolean {
        return oldItem == newItem
    }
}