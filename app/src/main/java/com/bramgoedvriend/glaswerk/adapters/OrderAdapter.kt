package com.bramgoedvriend.glaswerk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bramgoedvriend.glaswerk.databinding.ListItemOrdersBinding
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.ItemAndLokaal

class OrderAdapter(val clickListener: OrderListener) : ListAdapter<ItemAndLokaal, OrderAdapter.ViewHolder>(
    OrderDiffCallback()
) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemAndLokaal, clickListener: OrderListener) {
            binding.item = item.item
            binding.lokaalNaam = item.lokaalNaam
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ListItemOrdersBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<ItemAndLokaal>() {
    override fun areItemsTheSame(oldItem: ItemAndLokaal, newItem: ItemAndLokaal): Boolean {
        return oldItem.item.id == newItem.item.id
    }

    override fun areContentsTheSame(oldItem: ItemAndLokaal, newItem: ItemAndLokaal): Boolean {
        return oldItem == newItem
    }
}

class OrderListener(val clickListener: (item: Item) -> Unit) {
    fun onClick(item: Item) = clickListener(item)
}
