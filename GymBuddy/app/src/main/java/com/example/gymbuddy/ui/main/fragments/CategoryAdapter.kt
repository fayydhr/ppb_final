package com.example.gymbuddy.ui.main.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymbuddy.data.local.Category // Import kelas data Category
import com.example.gymbuddy.databinding.ItemCategoryBinding // Binding untuk item_category.xml

class CategoryAdapter(private val onItemClick: (Category) -> Unit) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    // Melacak posisi item yang sedang dipilih untuk mengelola tampilan "terpilih"
    private var selectedPosition = 0 // Default ke "All" sebagai yang terpilih pertama

    class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            // Set status 'selected' pada TextView, yang akan memicu selector drawable
            binding.tvCategoryName.isSelected = category.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)

        holder.itemView.setOnClickListener {
            // Logika untuk memperbarui status terpilih
            if (selectedPosition != RecyclerView.NO_POSITION) {
                // Batalkan seleksi item yang sebelumnya terpilih
                val oldSelectedCategory = getItem(selectedPosition)
                oldSelectedCategory.isSelected = false
                notifyItemChanged(selectedPosition) // Beri tahu adapter untuk memperbarui tampilan
            }

            // Pilih item yang baru diklik
            category.isSelected = true
            selectedPosition = holder.adapterPosition // Simpan posisi baru yang terpilih
            notifyItemChanged(selectedPosition) // Beri tahu adapter untuk memperbarui tampilan

            onItemClick(category) // Beri tahu fragmen bahwa item diklik
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name // Menggunakan nama sebagai ID unik untuk kategori
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}