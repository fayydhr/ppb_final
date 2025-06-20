package com.example.gymbuddy.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbuddy.data.local.Workout
import com.example.gymbuddy.databinding.ItemWorkoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutAdapter(
    private val onDeleteClick: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    class WorkoutViewHolder(
        private val binding: ItemWorkoutBinding,
        private val onDeleteClick: (Workout) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: Workout) {
            binding.tvExerciseName.text = workout.exerciseName
            // Tampilkan workoutType, scheduleDay, dan time
            binding.tvSetsReps.text = "Type: ${workout.workoutType} | Day: ${workout.scheduleDay}"
            binding.tvWeight.text = "Time: ${workout.time}"
            // Tampilkan progress di tempat yang sebelumnya sets/reps/weight
            binding.tvProgress.text = "Progress: ${workout.progress ?: "N/A"}" // Gunakan tvProgress
            binding.tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(workout.date)
            binding.tvNotes.text = workout.notes?.takeIf { it.isNotBlank() } ?: "No notes"

            binding.btnDelete.setOnClickListener {
                onDeleteClick(workout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }
}