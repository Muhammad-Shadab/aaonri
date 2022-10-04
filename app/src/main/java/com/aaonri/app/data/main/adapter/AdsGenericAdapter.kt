package com.aaonri.app.data.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.databinding.ImageOnlyViewHolderBinding
import com.aaonri.app.databinding.ImageWithTextBinding
import com.aaonri.app.databinding.TextOnlyItemBinding

class AdsGenericAdapter : RecyclerView.Adapter<AdvertiseViewHolder>() {

    var items = mutableListOf<FindAllActiveAdvertiseResponseItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((view: View, item: Any, position: Int) -> Unit)? =
        null

    fun load() {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        return when (viewType) {
            R.layout.text_only_item -> {
                AdvertiseViewHolder.TextOnlyViewHolder(
                    TextOnlyItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.image_only_view_holder -> {
                AdvertiseViewHolder.ImageOnlyViewHolder(
                    ImageOnlyViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.image_with_text -> {
                AdvertiseViewHolder.ImageAndTextViewHolder(
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

    override fun onBindViewHolder(holder: AdvertiseViewHolder, pos: Int) {
        if (items.size > 0) {
            holder.itemClickListener = itemClickListener
            val position = pos % items.size
            when (holder) {
                is AdvertiseViewHolder.ImageAndTextViewHolder -> holder.bind(items[position])
                is AdvertiseViewHolder.ImageOnlyViewHolder -> holder.bind(items[position])
                is AdvertiseViewHolder.TextOnlyViewHolder -> holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items == null) 0 else if (items.size in 1..2) items.size else Int.MAX_VALUE
    }

    override fun getItemViewType(pos: Int): Int {
        val position: Int
        if (items.size > 0) {
            position = pos % items.size
            return when (items[position].advertisementPageLocation.type) {
                "TXTONLY" -> R.layout.text_only_item
                "IMGONLY" -> R.layout.image_only_view_holder
                "BOTH" -> R.layout.image_with_text
                else -> R.layout.image_only_view_holder
            }

        }
        return R.layout.image_only_view_holder
    }
}