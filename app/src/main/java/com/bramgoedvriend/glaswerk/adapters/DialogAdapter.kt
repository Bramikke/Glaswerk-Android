package com.bramgoedvriend.glaswerk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bramgoedvriend.glaswerk.databinding.ListClassCardBinding
import com.bramgoedvriend.glaswerk.databinding.ListRoomCardBinding
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal

class RoomAdapter(val clickListener: ItemListenerGeneric<Lokaal>) : ListAdapter<Lokaal, RoomAdapter.ViewHolder>(
    DiffCallback<Lokaal>()
) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListRoomCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Lokaal, clickListener: ItemListenerGeneric<Lokaal>) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ListRoomCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ClassAdapter(val clickListener: ItemListenerGeneric<Klas>) : ListAdapter<Klas, ClassAdapter.ViewHolder>(
    DiffCallback<Klas>()
) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListClassCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Klas, clickListener: ItemListenerGeneric<Klas>) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ListClassCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallback <T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        if (oldItem is Lokaal && newItem is Lokaal) {
            return oldItem.lokaalId == newItem.lokaalId
        } else if (oldItem is Klas && newItem is Klas) {
            return oldItem.klasId == newItem.klasId
        }
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem is Any == newItem is Any
    }
}

class ItemListenerGeneric <T> (val clickListener: (item: T) -> Unit) {
    fun onClick(item: T) = clickListener(item)
}
