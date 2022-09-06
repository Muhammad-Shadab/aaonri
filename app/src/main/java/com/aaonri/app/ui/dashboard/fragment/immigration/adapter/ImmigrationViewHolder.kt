package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.DiscussionDetailsResponseItem
import com.aaonri.app.databinding.CategoryCardItemBinding
import com.aaonri.app.databinding.ImmigrationReplyItemBinding
import com.aaonri.app.databinding.ImmigrationsItemBinding
import java.time.format.DateTimeFormatter

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
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(discussion: Discussion) {
            binding.apply {
                val context = discussionNameTv.context
                discussionNameTv.text = discussion.discussionTopic
                discussionDesc.text = discussion.discussionDesc
                postedByTv.text = "Posted by: ${discussion.createdBy}, ${DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy").parse(discussion.createdOn))}"
                noOfReply.text = discussion.noOfReplies.toString()
                if (discussion.latestReply != null) {
                    latestReply.visibility = View.VISIBLE
                    latestReply.text =
                        "Last reply: ${discussion.latestReply.createdByName} ${ DateTimeFormatter.ofPattern("MM-dd-yyyy")
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(discussion.latestReply.createdDate.split("T")[0]))}"
                }

                root.setOnClickListener {
                    itemClickListener?.invoke(it, discussion, adapterPosition)
                }
            }
        }
    }

    class ImmigrationDetailScreenViewHolder(private val binding: ImmigrationReplyItemBinding) :
        ImmigrationViewHolder(binding) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(discussionDetailsResponseItem: DiscussionDetailsResponseItem) {
            binding.apply {
                discussionUserReplyTv.text = discussionDetailsResponseItem.userFullName
                userReplyDate.text = "${DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy").parse(discussionDetailsResponseItem.createdOn))}"
                userReplyDescTv.text = discussionDetailsResponseItem.replyDesc
                root.setOnClickListener {
                    itemClickListener?.invoke(it, discussionDetailsResponseItem, adapterPosition)
                }
            }
        }
    }


}