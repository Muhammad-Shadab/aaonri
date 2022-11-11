package com.aaonri.app.ui.authentication.register.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.CommunityAuth
import com.aaonri.app.databinding.CommunityItemBinding


class CommunityItemAdapter(private var selectedCommunity: ((value: List<CommunityAuth>) -> Unit)? = null) :
    RecyclerView.Adapter<CommunityItemAdapter.CustomViewHolder>() {

    private var data = listOf<CommunityAuth>()

    private var selectedCommunityList = mutableListOf<CommunityAuth>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.communityText.text = data[position].communityName

        if (selectedCommunityList.contains(data[position])) {
            holder.binding.communityText.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.blueBtnColor
                )
            )
            holder.binding.communityText.setTextColor(context.getColor(R.color.white))
        } else {
            holder.binding.communityText.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.binding.communityText.setTextColor(context.getColor(R.color.textViewColor))
        }

        holder.itemView.setOnClickListener {

            if (selectedCommunityList.contains(data[position])) {
                selectedCommunityList.remove(data[position])
                holder.binding.communityText.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.binding.communityText.setTextColor(context.getColor(R.color.textViewColor))
            } else {
                holder.binding.communityText.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blueBtnColor
                    )
                )
                holder.binding.communityText.setTextColor(context.getColor(R.color.white))
                selectedCommunityList.add(data[position])
            }
            selectedCommunity?.let { it1 -> it1(selectedCommunityList) }
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<CommunityAuth>) {
        this.data = data
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSavedList(selectedCommunityList: MutableList<CommunityAuth>) {
        this.selectedCommunityList = selectedCommunityList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: CommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}


