package com.example.scrollablexml.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.databinding.HomeCarouselCardBinding
import com.example.scrollablexml.model.ScrollableData

class CarouselViewHolder(
    private val binding: HomeCarouselCardBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ScrollableData) {
        binding.ivCarousel.setImageResource(item.detailImageResourceId)
    }
}