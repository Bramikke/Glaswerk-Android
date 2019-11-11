package com.bramgoedvriend.glaswerk.damage.damage_student

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bramgoedvriend.glaswerk.databinding.ListStudentBinding
import com.bramgoedvriend.glaswerk.domain.Student

class DamageStudentAdapter(val clickListener: StudentListener) :
    ListAdapter<Student, DamageStudentAdapter.ViewHolder>(DamageDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            student: Student,
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

class DamageDiffCallback : DiffUtil.ItemCallback<Student>() {
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.studentId == newItem.studentId
    }

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem == newItem
    }
}

class StudentListener(val clickListener: (student: Student) -> Unit) {
    fun onClick(student: Student) = clickListener(student)
}