package com.aaonri.app.ui.dashboard.fragment.homescreen_filter.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.event.model.UserEvent
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.aaonri.app.databinding.EventItemBinding
import com.aaonri.app.databinding.ImmigrationsItemBinding

class HomeFilterAdapter : RecyclerView.Adapter<HomeCategoryFilterViewHolder>() {

    private var data = listOf<Any>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoryFilterViewHolder {
        return when (viewType) {
            R.layout.classified_card_items -> {
                HomeCategoryFilterViewHolder.ClassifiedItemViewHolder(
                    ClassifiedCardItemsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.event_item -> {
                HomeCategoryFilterViewHolder.EventViewHolder(
                    EventItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.immigrations_item -> {
                HomeCategoryFilterViewHolder.AllImmigrationDiscussionViewHolder(
                    ImmigrationsItemBinding.inflate(
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
    override fun onBindViewHolder(holder: HomeCategoryFilterViewHolder, position: Int) {

        when (holder) {
            is HomeCategoryFilterViewHolder.AllImmigrationDiscussionViewHolder -> {
                if (data[position] is Discussion) {
                    holder.bind(data[position] as Discussion)
                }
            }
            is HomeCategoryFilterViewHolder.ClassifiedItemViewHolder -> {
                if (data[position] is UserAds) {
                    holder.bind(data[position] as UserAds)
                }
            }
            is HomeCategoryFilterViewHolder.EventViewHolder -> {
                if (data[position] is UserEvent) {
                    holder.bind(data[position] as UserEvent)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is UserAds -> R.layout.classified_card_items
            is UserEvent -> R.layout.event_item
            is Discussion -> R.layout.immigrations_item
            else -> {
                R.layout.classified_card_items
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

}