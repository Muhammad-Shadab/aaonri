package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.Community
import com.aaonri.app.databinding.CommunityItemBinding


class CommunityItemAdapter :
    RecyclerView.Adapter<CommunityItemAdapter.CustomViewHolder>() {

    private var data = listOf<Community>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        with(holder) {
            with(binding) {
                communityText.text = data[position].communityName
                itemView.setOnClickListener {
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
    fun setData(data: List<Community>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: CommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}


