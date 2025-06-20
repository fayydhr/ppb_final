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
            binding.tvSetsReps.text = "${workout.sets} sets x ${workout.reps} reps"
            binding.tvWeight.text = "${workout.weight} kg"
            binding.tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(workout.date)
            binding.tvNotes.text = workout.notes ?: "-"

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