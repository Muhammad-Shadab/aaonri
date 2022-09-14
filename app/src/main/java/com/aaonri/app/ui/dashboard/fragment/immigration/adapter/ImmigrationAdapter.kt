package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.*
import com.aaonri.app.databinding.*

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
            R.layout.category_card_item1 -> {
                ImmigrationViewHolder.ImmigrationCenterCategoryViewHolder(
                    CategoryCardItem1Binding.inflate(
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
            R.layout.immigrations_inforamtion_center_item -> {
                ImmigrationViewHolder.ImmigrationInformationCenterViewHolder(
                    ImmigrationsInforamtionCenterItemBinding.inflate(
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

            is ImmigrationViewHolder.ImmigrationCenterCategoryViewHolder -> {
                if (data[position] is Category) {
                    holder.bind(data[position] as Category)
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

            is ImmigrationViewHolder.ImmigrationInformationCenterViewHolder -> {
                if (data[position] is ImmigrationCenterModelItem) {
                    holder.bind(data[position] as ImmigrationCenterModelItem)
                }
            }

        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is DiscussionCategoryResponseItem -> R.layout.category_card_item
            is Discussion -> R.layout.immigrations_item
            is Category -> R.layout.category_card_item1
            is DiscussionDetailsResponseItem -> R.layout.immigration_reply_item
            is ImmigrationCenterModelItem -> R.layout.immigrations_inforamtion_center_item
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