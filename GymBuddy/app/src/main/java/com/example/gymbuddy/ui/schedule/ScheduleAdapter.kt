package com.example.gymbuddy.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbuddy.data.local.Schedule
import com.example.gymbuddy.databinding.ItemScheduleBinding // Pastikan ini benar
// Tidak ada SimpleDateFormat atau Locale yang dibutuhkan di sini karena tanggal tidak ada di Schedule

class ScheduleAdapter(
    private val onDeleteClick: (Schedule) -> Unit
) : ListAdapter<Schedule, ScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback()) {

    class ScheduleViewHolder(
        private val binding: ItemScheduleBinding,
        private val onDeleteClick: (Schedule) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {
            binding.tvDay.text = schedule.day
            binding.tvWorkoutType.text = schedule.workoutType
            binding.tvTime.text = schedule.time
            binding.tvScheduleNotes.text = schedule.notes.takeIf { it.isNotBlank() } ?: "No notes"

            binding.btnDeleteSchedule.setOnClickListener { // Ini harus sesuai dengan ID di item_schedule.xml
                onDeleteClick(schedule)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduleViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}