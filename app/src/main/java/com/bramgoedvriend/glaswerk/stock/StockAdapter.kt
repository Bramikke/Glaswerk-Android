package com.bramgoedvriend.glaswerk.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bramgoedvriend.glaswerk.damage.ItemListener
import com.bramgoedvriend.glaswerk.databinding.ListItemBinding
import com.bramgoedvriend.glaswerk.data.Item

class StockAdapter(val clickListener: ItemListener): ListAdapter<Item, StockAdapter.ViewHolder>(StockDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Item,
            clickListener: ItemListener
        ) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class StockDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}