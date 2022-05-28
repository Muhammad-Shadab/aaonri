package com.aaonri.app.ui.authentication.register.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.databinding.CommunityItemBinding
import com.aaonri.app.databinding.CommunitySelectedItemBinding

sealed class CommunityRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class CommunityItemViewHolder(private val binding: CommunityItemBinding) :
        CommunityRecyclerViewHolder(binding) {
        fun bind(title: CommunityRecyclerViewItem.CommunityItem) {
            binding.communityText.text = title.title
        }
    }

    class SelectCommunityItemViewHolder(private val binding: CommunitySelectedItemBinding) :
        CommunityRecyclerViewHolder(binding) {
        fun bind(title: CommunityRecyclerViewItem.SelectedCommunityItem) {
            binding.selectedCommunityText.text = title.title
        }
    }


}