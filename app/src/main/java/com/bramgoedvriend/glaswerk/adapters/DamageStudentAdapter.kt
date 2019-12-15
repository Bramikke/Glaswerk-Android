package com.bramgoedvriend.glaswerk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bramgoedvriend.glaswerk.databinding.ListStudentBinding
import com.bramgoedvriend.glaswerk.domain.StudentAndStudentItem

class DamageStudentAdapter(val clickListener: StudentListener) :
    ListAdapter<StudentAndStudentItem, DamageStudentAdapter.ViewHolder>(
        DamageStudentDiffCallback()
    ) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: ListStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            student: StudentAndStudentItem,
            clickListener: StudentListener
        ) {
            binding.student = student
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ListStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DamageStudentDiffCallback : DiffUtil.ItemCallback<StudentAndStudentItem>() {
    override fun areItemsTheSame(oldItem: StudentAndStudentItem, newItem: StudentAndStudentItem): Boolean {
        return oldItem.student.leerlingId == newItem.student.leerlingId
    }

    override fun areContentsTheSame(oldItem: StudentAndStudentItem, newItem: StudentAndStudentItem): Boolean {
        return oldItem == newItem
    }
}

class StudentListener(val clickListener: (student: StudentAndStudentItem) -> Unit) {
    fun onClick(student: StudentAndStudentItem) = clickListener(student)
}
