package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.CommunityItemBinding

class CommunityItemAdapter : RecyclerView.Adapter<CommunityItemAdapter.CustomViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.communityText.text = data[position]
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: CommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}