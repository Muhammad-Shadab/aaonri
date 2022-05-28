package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.databinding.CommunityItemBinding
import com.aaonri.app.databinding.CommunitySelectedItemBinding
import com.aaonri.app.ui.authentication.register.recyclerview.CommunityRecyclerViewHolder
import com.aaonri.app.ui.authentication.register.recyclerview.CommunityRecyclerViewItem

/*
class CommunityItemAdapter(private var selectedCommunity: ((value: String) -> Unit)? = null) :
    RecyclerView.Adapter<CommunityItemAdapter.CustomViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        with(holder) {
            with(binding) {
                communityText.text = data[position]
                itemView.setOnClickListener {
                    selectedCommunity?.let { it1 -> it1(data[position]) }
                    communityText.setTextColor(ContextCompat.getColor(context, R.color.white))
                    communityText.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.blueBtnColor
                        )
                    )
                }
            }
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: CommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}*/

class HomeRecyclerViewAdapter : RecyclerView.Adapter<CommunityRecyclerViewHolder>() {

    var items = listOf<CommunityRecyclerViewItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((view: View, item: CommunityRecyclerViewItem, position: Int) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityRecyclerViewHolder {
        return when (viewType) {
            R.layout.community_item -> CommunityRecyclerViewHolder.CommunityItemViewHolder(
                CommunityItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.community_selected_item -> CommunityRecyclerViewHolder.SelectCommunityItemViewHolder(
                CommunitySelectedItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw IllegalArgumentException("Invalid View type")
        }
    }

    override fun onBindViewHolder(holder: CommunityRecyclerViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        when (holder) {
            is CommunityRecyclerViewHolder.CommunityItemViewHolder -> holder.bind(items[position] as CommunityRecyclerViewItem.CommunityItem)
            is CommunityRecyclerViewHolder.SelectCommunityItemViewHolder -> holder.bind(items[position] as CommunityRecyclerViewItem.SelectedCommunityItem)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CommunityRecyclerViewItem.CommunityItem -> R.layout.community_item
            is CommunityRecyclerViewItem.SelectedCommunityItem -> R.layout.community_selected_item
        }
    }

}
