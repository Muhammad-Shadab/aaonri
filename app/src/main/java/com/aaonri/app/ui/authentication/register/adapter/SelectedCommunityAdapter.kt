package com.aaonri.app.ui.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.authentication.register.model.CommunityAuth
import com.aaonri.app.databinding.CommunitySelectedItemBinding

class SelectedCommunityAdapter :
    RecyclerView.Adapter<SelectedCommunityAdapter.SelectedCommunityAdapter>() {

    private var data = listOf<CommunityAuth>()

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
    fun setData(data: List<CommunityAuth>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class SelectedCommunityAdapter(val binding: CommunitySelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}