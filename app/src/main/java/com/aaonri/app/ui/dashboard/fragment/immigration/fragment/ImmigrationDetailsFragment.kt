package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.ReplyDiscussionRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationDetailsFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImmigrationDetailsFragment : Fragment() {
    var binding: FragmentImmigrationDetailsFrgamentBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    val args: ImmigrationDetailsFragmentArgs by navArgs()
    var discussion: Discussion? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentImmigrationDetailsFrgamentBinding.inflate(layoutInflater, container, false)

        immigrationAdapter = ImmigrationAdapter()

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }

        binding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            postReplyBtn.setOnClickListener {
                if (postReplyEt.text.toString().isNotEmpty()) {
                    immigrationViewModel.replyDiscussion(
                        ReplyDiscussionRequest(
                            createdByUserId = userId ?: 0,
                            discussionId = if (discussion?.discussionId != null) discussion?.discussionId.toString() else 0.toString(),
                            id = 0,
                            parentId = 0,
                            replyDesc = postReplyEt.text.toString(),
                        )
                    )
                    postReplyEt.setText("")
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter valid reply", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            allReplyRv.layoutManager = LinearLayoutManager(context)
            allReplyRv.adapter = immigrationAdapter

            immigrationViewModel.selectedDiscussionItem.observe(viewLifecycleOwner) {
                discussion = it
                immigrationViewModel.getDiscussionDetailsById(it.discussionId.toString())
                discussionNameTv.text = it.discussionTopic
                postedByTv.text = "Posted by: ${it.createdOn}"
                discussionDesc.text = it.discussionDesc
                noOfReply.text = it.noOfReplies.toString()
                discussionDetailsLl.visibility = View.VISIBLE
            }
        }

        immigrationViewModel.replyDiscussionData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    immigrationViewModel.getDiscussionDetailsById(discussion?.discussionId.toString())
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        immigrationViewModel.discussionDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { immigrationAdapter?.setData(it) }
                    binding?.immigrationNestedScroll?.fullScroll(View.FOCUS_DOWN)
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (args.isFromAllDiscussionScreen) {
                        immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(true)
                    } else {
                        immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(true)
                    }
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        immigrationViewModel.discussionDetailsData.value = null
    }

}