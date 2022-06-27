package com.aaonri.app.ui.dashboard.fragment.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.EventsPagerItemBinding
import com.bumptech.glide.Glide

/*
class EventPagerAdapter :
    RecyclerView.Adapter<EventPagerAdapter.ViewPagerViewHolder>() {

    var listOfImages = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventsPagerItemBinding.inflate(inflater, parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            Glide.with(context).load(listOfImages[position]).into(eventImage)
        }
    }

    fun setData(listOfImage: MutableList<Int>) {
        this.listOfImages = listOfImage
    }

    override fun getItemCount() = listOfImages.size

    inner class ViewPagerViewHolder(val binding: EventsPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}*/
