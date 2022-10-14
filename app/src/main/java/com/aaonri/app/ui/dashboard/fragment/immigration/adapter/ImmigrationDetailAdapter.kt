package com.aaonri.app.ui.dashboard.fragment.immigration.adapter

/*
class ImmigrationDetailAdapter(private var deleteComment: ((value: DiscussionDetailsResponseItem) -> Unit)) :
    RecyclerView.Adapter<ImmigrationDetailAdapter.ImmigrationDetailViewHolder>() {

    var data = listOf<DiscussionDetailsResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmigrationDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImmigrationReplyItemBinding.inflate(inflater, parent, false)
        return ImmigrationDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImmigrationDetailViewHolder, position: Int) {
        val context = holder.binding.discussionUserReplyTv.context

        val nameParts = data[position].userFullName.split(" ").toTypedArray()
        val firstName = nameParts[0]
        val firstNameChar = firstName[0]
        var lastNameChar = ""
        if (nameParts.size > 1) {
            val lastName = nameParts[nameParts.size - 1]
            lastNameChar = lastName[0].toString()
        }

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }
        holder.binding.apply {

            userNameTv.text = firstNameChar + lastNameChar
            discussionUserReplyTv.text = data[position].userFullName
            userReplyDate.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                .format(
                    DateTimeFormatter.ofPattern("dd MMM yyyy")
                        .parse(data[position].createdOn)
                )
            userReplyDescTv.text = data[position].replyDesc
           */
/* replyBtn.setOnClickListener {
                itemClickListener?.invoke(
                    it,
                    discussionDetailsResponseItem,
                    adapterPosition,
                    false,
                    false
                )
            }*//*


            deleteComment.setOnClickListener {
                deleteComment(data[position])
            }

            if (userId != 0) {
                if (data[position].createdBy == userId) {
                    deleteComment.visibility = View.VISIBLE
                    Toast.makeText(
                        context,
                        "${data[position].userFullName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // deleteReplyClickListener?.invoke()
            */
/*root.setOnClickListener {
                itemClickListener?.invoke(
                    it,
                    discussionDetailsResponseItem,
                    adapterPosition,
                    false,
                    false
                )
            }*//*

        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: List<DiscussionDetailsResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ImmigrationDetailViewHolder(val binding: ImmigrationReplyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}*/
