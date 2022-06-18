package com.aaonri.app.data.classified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.FilterCardViewItemBinding
import java.util.logging.Filter

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    var data = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterCardViewItemBinding.inflate(inflater, parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.binding.filterText.text = data[position]

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: MutableList<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class FilterViewHolder(val binding: FilterCardViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
