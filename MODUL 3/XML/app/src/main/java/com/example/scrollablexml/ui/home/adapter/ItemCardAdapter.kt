package com.example.scrollablexml.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.databinding.HomeItemCardBinding
import com.example.scrollablexml.model.ScrollableData
import com.example.scrollablexml.ui.home.viewholder.ItemCardViewHolder

class ItemCardAdapter(
    private val items: List<ScrollableData>,
    private val onDetailClicked: (Int) -> Unit,
    private val onSteamClicked: (String) -> Unit
): RecyclerView.Adapter<ItemCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardViewHolder {
        val binding = HomeItemCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemCardViewHolder, position: Int) {
        holder.bind(items[position], position, onDetailClicked, onSteamClicked)
    }

    override fun getItemCount() = items.size
}