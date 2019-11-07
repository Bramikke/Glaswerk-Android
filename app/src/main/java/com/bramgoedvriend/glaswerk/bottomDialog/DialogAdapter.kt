package com.bramgoedvriend.glaswerk.bottomDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bramgoedvriend.glaswerk.databinding.ListClassCardBinding
import com.bramgoedvriend.glaswerk.databinding.ListRoomCardBinding
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal

class RoomAdapter: ListAdapter<Lokaal, RoomAdapter.ViewHolder>(DiffCallback <Lokaal>()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListRoomCardBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Lokaal) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup): ViewHolder {
                val binding =
                    ListRoomCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ClassAdapter: ListAdapter<Klas, ClassAdapter.ViewHolder>(DiffCallback<Klas>()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListClassCardBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Klas) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup): ViewHolder {
                val binding =
                    ListClassCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallback <T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        if(oldItem is Lokaal && newItem is Lokaal) {
            return oldItem.lokaalid == newItem.lokaalid
        }
        else if(oldItem is Klas && newItem is Klas) {
            return oldItem.klasid == newItem.klasid
        }
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem is Any == newItem is Any
    }
}