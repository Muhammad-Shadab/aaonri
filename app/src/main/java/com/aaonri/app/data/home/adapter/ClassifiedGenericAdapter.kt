package com.aaonri.app.data.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.data.event.model.UserEvent
import com.aaonri.app.databinding.ClassifiedAdvertiseItemBinding
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.aaonri.app.databinding.EventAdvertiseItemBinding
import com.aaonri.app.databinding.EventItemBinding

class ClassifiedGenericAdapter : RecyclerView.Adapter<HomeScreenViewHolders>() {

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

    @RequiresApi(Build.VERSION_CODES.O)
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
            else -> {}
        }
    }

    override fun getItemCount(): Int = if (items?.isNotEmpty() == true) 4 else 0

    override fun getItemViewType(position: Int): Int {
        return when (items?.get(position)) {
            is UserAds -> R.layout.classified_card_items
            is FindAllActiveAdvertiseResponseItem -> R.layout.classified_advertise_item
            else -> 0
        }
    }

}

class EventGenericAdapter : RecyclerView.Adapter<HomeScreenViewHolders>() {

    var items: List<Any>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScreenViewHolders {
        return when (viewType) {
            R.layout.event_item -> {
                HomeScreenViewHolders.EventViewHolder(
                    EventItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.event_advertise_item -> {
                HomeScreenViewHolders.EventAdvertiseViewHolder(
                    EventAdvertiseItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeScreenViewHolders, position: Int) {
        when (holder) {
            is HomeScreenViewHolders.EventViewHolder -> {
                if (items?.get(position) is UserEvent) {
                    holder.bind(items!![position] as UserEvent)
                }
            }

            is HomeScreenViewHolders.EventAdvertiseViewHolder -> {
                if (items?.get(position) is FindAllActiveAdvertiseResponseItem) {
                    holder.bind(items!![position] as FindAllActiveAdvertiseResponseItem)
                }
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int = if (items?.isNotEmpty() == true) 4 else 0

    override fun getItemViewType(position: Int): Int {
        return when (items?.get(position)) {
            is UserEvent -> R.layout.event_item
            is FindAllActiveAdvertiseResponseItem -> R.layout.event_advertise_item
            else -> 0
        }
    }

}