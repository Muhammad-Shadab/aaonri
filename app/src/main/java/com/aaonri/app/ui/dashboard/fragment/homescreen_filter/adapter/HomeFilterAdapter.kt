package com.aaonri.app.ui.dashboard.fragment.homescreen_filter.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.databinding.EventItemBinding
import com.aaonri.app.databinding.ImmigrationsItemBinding

class HomeFilterAdapter : RecyclerView.Adapter<HomeCategoryFilterViewHolder>() {

    private var data = listOf<Any>()

    var itemClickListener: ((view: View, item: Any) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoryFilterViewHolder {
        return when (viewType) {
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
        holder.itemClickListener = itemClickListener
        when (holder) {
            is HomeCategoryFilterViewHolder.AllImmigrationDiscussionViewHolder -> {
                if (data[position] is Discussion) {
                    holder.bind(data[position] as Discussion)
                }
            }
            is HomeCategoryFilterViewHolder.EventViewHolder -> {
                if (data[position] is Event) {
                    holder.bind(data[position] as Event)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Event -> R.layout.event_item
            is Discussion -> R.layout.immigrations_item
            else -> {
                R.layout.event_item
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

}