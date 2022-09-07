package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.DiscussionDetailsResponseItem
import com.aaonri.app.databinding.CategoryCardItemBinding
import com.aaonri.app.databinding.ImmigrationReplyItemBinding
import com.aaonri.app.databinding.ImmigrationsItemBinding

class ImmigrationAdapter : RecyclerView.Adapter<ImmigrationViewHolder>() {

    private var data = listOf<Any>()

    var itemClickListener: ((view: View, item: Any, position: Int, isUpdateImmigration: Boolean, isDeleteImmigration: Boolean) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmigrationViewHolder {
        return when (viewType) {
            R.layout.category_card_item -> {
                ImmigrationViewHolder.ImmigrationCategoryViewHolder(
                    CategoryCardItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.immigrations_item -> {
                ImmigrationViewHolder.AllImmigrationDiscussionViewHolder(
                    ImmigrationsItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.immigration_reply_item -> {
                ImmigrationViewHolder.ImmigrationDetailScreenViewHolder(
                    ImmigrationReplyItemBinding.inflate(
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
    override fun onBindViewHolder(holder: ImmigrationViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        when (holder) {
            is ImmigrationViewHolder.ImmigrationCategoryViewHolder -> {
                if (data[position] is DiscussionCategoryResponseItem) {
                    holder.bind(data[position] as DiscussionCategoryResponseItem)
                }
            }
            is ImmigrationViewHolder.AllImmigrationDiscussionViewHolder -> {
                if (data[position] is Discussion) {
                    holder.bind(data[position] as Discussion)
                }
            }
            is ImmigrationViewHolder.ImmigrationDetailScreenViewHolder -> {
                if (data[position] is DiscussionDetailsResponseItem) {
                    holder.bind(data[position] as DiscussionDetailsResponseItem)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is DiscussionCategoryResponseItem -> R.layout.category_card_item
            is Discussion -> R.layout.immigrations_item
            is DiscussionDetailsResponseItem -> R.layout.immigration_reply_item
            else -> {
                R.layout.category_card_item
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

}