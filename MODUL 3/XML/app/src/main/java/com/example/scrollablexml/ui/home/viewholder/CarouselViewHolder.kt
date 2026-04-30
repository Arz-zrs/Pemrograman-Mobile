package com.example.scrollablexml.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.databinding.HomeCarouselCardBinding
import com.example.scrollablexml.model.ScrollableData

class CarouselViewHolder(
    private val binding: HomeCarouselCardBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ScrollableData, index: Int, onDetailClicked: (Int) -> Unit) {
        binding.ivCarousel.setImageResource(item.detailImageResourceId)
        binding.root.setOnClickListener { onDetailClicked(index) }
    }
}