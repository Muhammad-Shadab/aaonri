package com.aaonri.app.data.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.databinding.ClassifiedAdvertiseItemBinding
import com.aaonri.app.databinding.ClassifiedCardItemsBinding

class GenericAdapter : RecyclerView.Adapter<HomeScreenViewHolders>() {

    var items: List<Any>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScreenViewHolders {
        return when (viewType) {
            R.layout.classified_card_items -> {
                HomeScreenViewHolders.ClassifiedViewHolder(
                    ClassifiedCardItemsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.classified_advertise_item -> {
                HomeScreenViewHolders.ClassifiedAdvertiseViewHolder(
                    ClassifiedAdvertiseItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: HomeScreenViewHolders, position: Int) {
        when (holder) {
            is HomeScreenViewHolders.ClassifiedViewHolder -> {
                if (items?.get(position) is UserAds) {
                    holder.bind(items!![position] as UserAds)
                }
            }

            is HomeScreenViewHolders.ClassifiedAdvertiseViewHolder -> {
                if (items?.get(position) is FindAllActiveAdvertiseResponseItem) {
                    holder.bind(items!![position] as FindAllActiveAdvertiseResponseItem)
                }
            }
        }
    }

    override fun getItemCount(): Int = if (items?.isNotEmpty() == true) 4  else 0

    override fun getItemViewType(position: Int): Int {
        return when (items?.get(position)) {
            is UserAds -> R.layout.classified_card_items
            is FindAllActiveAdvertiseResponseItem -> R.layout.classified_advertise_item
            else -> 0
        }
    }

}