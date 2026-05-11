package com.example.scrollablexml.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.databinding.HomeCarouselHeaderBinding
import com.example.scrollablexml.model.ScrollableData
import com.example.scrollablexml.ui.home.adapter.CarouselAdapter

class CarouselHeaderViewHolder (
    private val binding: HomeCarouselHeaderBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(items: List<ScrollableData>, onDetailClicked: (Int) -> Unit){
        val _adapter = CarouselAdapter(items, onDetailClicked)
        binding.rvCarousel.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = _adapter
        }
    }
    companion object {
        fun from(parent: ViewGroup): CarouselHeaderViewHolder {
            val binding = HomeCarouselHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            LinearSnapHelper().attachToRecyclerView(binding.rvCarousel)
            return CarouselHeaderViewHolder(binding)
        }
    }
}