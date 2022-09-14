package com.aaonri.app.ui.dashboard.fragment.event.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.event.model.UserEvent
import com.aaonri.app.ui.dashboard.home.adapter.HomeScreenViewHolders
import com.aaonri.app.databinding.EventAdvertiseItemBinding
import com.aaonri.app.databinding.EventItemBinding

class EventGenericAdapter : RecyclerView.Adapter<HomeScreenViewHolders>() {

    var items: List<Any>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListenerEvent: ((view: View, item: UserEvent, position: Int) -> Unit)? =
        null

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
        holder.itemClickListenerEvent = itemClickListenerEvent
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
            else -> R.layout.event_item
        }
    }

}