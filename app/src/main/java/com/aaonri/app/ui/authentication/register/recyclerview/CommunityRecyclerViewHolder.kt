package com.aaonri.app.ui.authentication.register.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.CommunityItemBinding
import com.aaonri.app.databinding.CommunitySelectedItemBinding

sealed class CommunityRecyclerViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: CommunityRecyclerViewItem, position: Int) -> Unit)? =
        null

    class CommunityItemViewHolder(private val binding: CommunityItemBinding) :
        CommunityRecyclerViewHolder(binding) {
        val context: Context = binding.root.context

        @SuppressLint("ResourceAsColor")
        fun bind(title: CommunityRecyclerViewItem.CommunityItem) {
            binding.communityText.text = title.title
            binding.communityText.setOnClickListener {
                itemClickListener?.invoke(it, title, adapterPosition)
                binding.communityText.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.communityText.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blueBtnColor
                    )
                )
            }
        }
    }

    class SelectCommunityItemViewHolder(private val binding: CommunitySelectedItemBinding) :
        CommunityRecyclerViewHolder(binding) {
        fun bind(title: CommunityRecyclerViewItem.SelectedCommunityItem) {
            binding.selectedCommunityText.text = title.title
        }
    }


}