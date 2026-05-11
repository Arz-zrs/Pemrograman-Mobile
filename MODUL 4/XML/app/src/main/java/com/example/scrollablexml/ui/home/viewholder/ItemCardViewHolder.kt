package com.example.scrollablexml.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.databinding.HomeItemCardBinding
import com.example.scrollablexml.model.ScrollableData

class ItemCardViewHolder(
    private val binding: HomeItemCardBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: ScrollableData,
        index: Int,
        onDetailClicked: (Int) -> Unit,
        onSteamClicked: (String) -> Unit
    ) {
        binding.apply {
            ivItem.setImageResource(item.imageResourceId)
            tvTitle.setText(item.titleResourceId)
            tvSubtitle.setText(item.subtitleResourceId)
            tvDesc.setText(item.descriptionResourceId)
            btnDetail.setOnClickListener { onDetailClicked(index) }
            btnSteam.setOnClickListener { onSteamClicked(item.steamUrl) }
        }
    }
}