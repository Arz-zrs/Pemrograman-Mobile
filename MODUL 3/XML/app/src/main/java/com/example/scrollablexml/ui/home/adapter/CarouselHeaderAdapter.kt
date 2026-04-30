package com.example.scrollablexml.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scrollablexml.model.ScrollableData
import com.example.scrollablexml.ui.home.viewholder.CarouselHeaderViewHolder

class CarouselHeaderAdapter(
    private val items: List<ScrollableData>
): RecyclerView.Adapter<CarouselHeaderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        = CarouselHeaderViewHolder.from(parent)

    override fun onBindViewHolder(holder: CarouselHeaderViewHolder, position: Int)
        = holder.bind(items)

    override fun getItemCount() = 1
}
