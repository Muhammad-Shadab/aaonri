package com.aaonri.app.data.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.databinding.ImageOnlyViewHolderBinding
import com.aaonri.app.databinding.ImageWithTextBinding
import com.aaonri.app.databinding.TextOnlyItemBinding

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewHolder>() {

    var items = listOf<FindAllActiveAdvertiseResponseItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return when (viewType) {
            R.layout.text_only_item -> {
                HomeRecyclerViewHolder.TextOnlyViewHolder(
                    TextOnlyItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.image_only_view_holder -> {
                HomeRecyclerViewHolder.ImageOnlyViewHolder(
                    ImageOnlyViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.image_with_text -> {
                HomeRecyclerViewHolder.ImageAndTextViewHolder(
                    ImageWithTextBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        when(holder){
            is HomeRecyclerViewHolder.ImageAndTextViewHolder -> holder.bind(items[position])
            is HomeRecyclerViewHolder.ImageOnlyViewHolder -> holder.bind(items[position])
            is HomeRecyclerViewHolder.TextOnlyViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position].checkViewType) {
            CheckViewType.TEXT_ONLY -> R.layout.text_only_item
            CheckViewType.IMAGE_ONLY -> R.layout.image_only_view_holder
            CheckViewType.BOTH -> R.layout.image_with_text
        }
    }

}