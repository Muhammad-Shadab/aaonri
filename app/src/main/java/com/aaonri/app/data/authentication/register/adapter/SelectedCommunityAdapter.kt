package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.Community
import com.aaonri.app.databinding.CommunitySelectedItemBinding

class SelectedCommunityAdapter :
    RecyclerView.Adapter<SelectedCommunityAdapter.SelectedCommunityAdapter>() {

    private var data = listOf<Community>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedCommunityAdapter {
        val binding =
            CommunitySelectedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedCommunityAdapter(binding)
    }

    override fun onBindViewHolder(holder: SelectedCommunityAdapter, position: Int) {
        val context = holder.itemView.context
        holder.binding.selectedCommunityText.text = data[position].communityName

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Community>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class SelectedCommunityAdapter(val binding: CommunitySelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}