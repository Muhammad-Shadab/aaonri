package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.immigration.model.*
import com.aaonri.app.databinding.*
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.noowenz.showmoreless.ShowMoreLess
import java.time.format.DateTimeFormatter

sealed class ImmigrationViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: Any, position: Int, isUpdateImmigration: Boolean, isDeleteImmigration: Boolean) -> Unit)? =
        null

    var deleteReplyClickListener: ((item: Any) -> Unit)? = null

    class ImmigrationCategoryViewHolder(private val binding: CategoryCardItemBinding) :
        ImmigrationViewHolder(binding) {
        fun bind(discussionCategoryResponseItem: DiscussionCategoryResponseItem) {
            binding.apply {
                countryTv.text = discussionCategoryResponseItem.discCatValue
                root.setOnClickListener {
                    itemClickListener?.invoke(
                        it,
                        discussionCategoryResponseItem,
                        adapterPosition,
                        false,
                        false
                    )
                }

            }
        }
    }

    class ImmigrationCenterCategoryViewHolder(private val binding: CategoryCardItem1Binding) :
        ImmigrationViewHolder(binding) {
        fun bind(categoryItem: Category) {
            binding.apply {
                countryTv.text = Html.fromHtml(categoryItem.title)
                root.setOnClickListener {
                    itemClickListener?.invoke(
                        it,
                        categoryItem,
                        adapterPosition,
                        false,
                        false
                    )
                }

            }
        }
    }

    class AllImmigrationDiscussionViewHolder(private val binding: ImmigrationsItemBinding) :
        ImmigrationViewHolder(binding) {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(discussion: Discussion) {
            binding.apply {
                val context = discussionNameTv.context
                val userEmail =
                    context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

                discussionNameTv.text = discussion.discussionTopic
                discussionDesc.text = discussion.discussionDesc
                postedByTv.text = "Posted by: ${discussion.createdBy}, ${
                    DateTimeFormatter.ofPattern("MM-dd-yyyy")
                        .format(
                            DateTimeFormatter.ofPattern("dd-MMM-yyyy").parse(discussion.createdOn)
                        )
                }"
                noOfReply.text = discussion.noOfReplies.toString()
                if (discussion.latestReply != null) {
                    latestReply.visibility = View.VISIBLE
                    latestReply.text =
                        "Last reply: ${discussion.latestReply.createdByName}  ${
                            DateTimeFormatter.ofPattern("MM-dd-yyyy")
                                .format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        .parse(discussion.latestReply.createdDate.split("T")[0])
                                )
                        }"
                }

                userEmail.let {

                    /*swipeLayout.isRightSwipeEnabled =
                        !discussion.approved && discussion.userId == it*/

                    swipeLayout.isRightSwipeEnabled = discussion.noOfReplies == 0

                    if (discussion.approved) {
                        updateImmigrationBtn.visibility = View.GONE
                    }
                }


                immigrationCv.setOnClickListener {
                    itemClickListener?.invoke(it, discussion, adapterPosition, false, false)
                }

                updateImmigrationBtn.setOnClickListener {
                    itemClickListener?.invoke(it, discussion, adapterPosition, true, false)
                }

                deleteImmigrationBtn.setOnClickListener {
                    itemClickListener?.invoke(it, discussion, adapterPosition, false, true)
                }

            }
        }
    }

    class ImmigrationDetailScreenViewHolder(private val binding: ImmigrationReplyItemBinding) :
        ImmigrationViewHolder(binding) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(discussionDetailsResponseItem: DiscussionDetailsResponseItem) {
            val context = binding.discussionUserReplyTv.context

            val nameParts = discussionDetailsResponseItem.userFullName.split(" ").toTypedArray()
            val firstName = nameParts[0]
            val firstNameChar = firstName[0]
            var lastNameChar = ""
            if (nameParts.size > 1) {
                val lastName = nameParts[nameParts.size - 1]
                lastNameChar = lastName[0].toString()
            }

            val userId =
                context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }
            binding.apply {

                userNameTv.text = firstNameChar + lastNameChar
                discussionUserReplyTv.text = discussionDetailsResponseItem.userFullName
                userReplyDate.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                            .parse(discussionDetailsResponseItem.createdOn)
                    )
                userReplyDescTv.text = discussionDetailsResponseItem.replyDesc
                replyBtn.setOnClickListener {
                    itemClickListener?.invoke(
                        it,
                        discussionDetailsResponseItem,
                        adapterPosition,
                        false,
                        false
                    )
                }

                deleteComment.setOnClickListener {
                    deleteReplyClickListener?.invoke(discussionDetailsResponseItem)
                }

                if (userId != 0) {
                    if (discussionDetailsResponseItem.createdBy == userId) {
                        deleteComment.visibility = View.VISIBLE
                    }
                }

                // deleteReplyClickListener?.invoke()
                /*root.setOnClickListener {
                    itemClickListener?.invoke(
                        it,
                        discussionDetailsResponseItem,
                        adapterPosition,
                        false,
                        false
                    )
                }*/
            }
        }
    }

    class ImmigrationInformationCenterViewHolder(private val binding: ImmigrationsInforamtionCenterItemBinding) :
        ImmigrationViewHolder(binding) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(immigrationCenterItem: ImmigrationCenterModelItem) {
            binding.apply {
                visaNameTv.text = immigrationCenterItem.title
                classNameTv.text = immigrationCenterItem.subtitle
                root.setOnClickListener {
                    itemClickListener?.invoke(
                        it,
                        immigrationCenterItem,
                        adapterPosition,
                        false,
                        false
                    )
                }

            }
        }
    }


    open fun printInitials(name: String) {

    }


}