package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.databinding.CategoryCardItemBinding
import com.aaonri.app.databinding.ImmigrationsItemBinding

sealed class ImmigrationViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: Any, position: Int) -> Unit)? =
        null

    class ImmigrationCategoryViewHolder(private val binding: CategoryCardItemBinding) :
        ImmigrationViewHolder(binding) {
        fun bind(discussionCategoryResponseItem: DiscussionCategoryResponseItem) {
            binding.apply {
                countryTv.text = discussionCategoryResponseItem.discCatValue

                root.setOnClickListener {
                    itemClickListener?.invoke(it, discussionCategoryResponseItem, adapterPosition)
                }

            }
        }
    }

    class AllImmigrationDiscussionViewHolder(private val binding: ImmigrationsItemBinding) :
        ImmigrationViewHolder(binding) {
        fun bind(discussion: Discussion) {
            binding.apply {
                discussionNameTv.text = discussion.discussionTopic
                discussionDesc.text = discussion.discussionDesc
                postedByTv.text = "Posted by: ${discussion.createdBy}, ${discussion.createdOn}"
                noOfReply.text = discussion.noOfReplies.toString()
                if (discussion.latestReply != null) {
                    latestReply.visibility = View.VISIBLE
                    latestReply.text =
                        "Last reply: ${discussion.latestReply.createdByName} ${discussion.latestReply.createdDate}"
                }

                root.setOnClickListener {
                    itemClickListener?.invoke(it, discussion, adapterPosition)
                }
            }
        }
    }


}