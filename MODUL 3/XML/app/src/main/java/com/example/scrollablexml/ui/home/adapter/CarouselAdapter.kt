package com.example.scrollablexml.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.databinding.HomeCarouselCardBinding
import com.example.scrollablexml.model.ScrollableData
import com.example.scrollablexml.ui.home.viewholder.CarouselViewHolder

class CarouselAdapter(
    private val items: List<ScrollableData>
): RecyclerView.Adapter<CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = HomeCarouselCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

