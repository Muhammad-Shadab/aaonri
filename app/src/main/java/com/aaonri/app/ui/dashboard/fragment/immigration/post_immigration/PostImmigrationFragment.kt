package com.aaonri.app.ui.dashboard.fragment.immigration.post_immigration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.PostDiscussionRequest
import com.aaonri.app.data.immigration.model.UpdateDiscussionRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentPostImmigrationBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar

class PostImmigrationFragment : Fragment() {
    var binding: FragmentPostImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var discussionCategoryResponseItem: DiscussionCategoryResponseItem? = null
    val args: PostImmigrationFragmentArgs by navArgs()
    var discussionData: Discussion? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostImmigrationBinding.inflate(layoutInflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        binding?.apply {

            navigateBack.setOnClickListener {
                activity?.onBackPressed()
            }

            selectImmigrationCategory.setOnClickListener {
                val action =
                    PostImmigrationFragmentDirections.actionPostImmigrationFragmentToImmigrationCategoryBottomSheet(
                        "PostImmigrationScreen"
                    )
                findNavController().navigate(action)
            }

            submitBtn.setOnClickListener {
                if (discussionTopicEt.text.toString().length >= 3) {
                    if (selectImmigrationCategory.text.toString().isNotEmpty()) {
                        if (descEt.text.toString().length >= 3) {

                            if (args.isUpdateImmigration) {
                                /*immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(
                                    false
                                )
                                immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(
                                    false
                                )*/
                                immigrationViewModel.updateDiscussion(
                                    UpdateDiscussionRequest(
                                        discCatId = if (discussionCategoryResponseItem?.discCatId != null) discussionCategoryResponseItem!!.discCatId else if (discussionData?.discCatId != null) discussionData!!.discCatId else 0,
                                        discussionDesc = descEt.text.toString(),
                                        discussionTopic = discussionTopicEt.text.toString(),
                                        userId = email ?: "",
                                        discussionId = if (discussionData?.discussionId != null) discussionData?.discussionId!! else 0
                                    )
                                )
                            } else {
                                /*immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(
                                    false
                                )
                                immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(
                                    false
                                )*/
                                immigrationViewModel.postDiscussion(
                                    PostDiscussionRequest(
                                        discCatId = if (discussionCategoryResponseItem?.discCatId != null) discussionCategoryResponseItem!!.discCatId else 0,
                                        discussionDesc = descEt.text.toString(),
                                        discussionTopic = discussionTopicEt.text.toString(),
                                        userId = email ?: ""
                                    )
                                )
                            }
                        } else {
                            showAlert("Please enter valid description")
                        }
                    } else {
                        showAlert("Please choose category")
                    }
                } else {
                    showAlert("Please enter valid discussion topic")
                }
            }

            if (args.isUpdateImmigration) {
                registrationText.text = "Update Discussion"
                immigrationViewModel.selectedDiscussionItem.observe(viewLifecycleOwner) { discussion ->
                    discussionData = discussion
                    discussionTopicEt.setText(discussion.discussionTopic)
                    selectImmigrationCategory.text = discussion.discCatValue
                    descEt.setText(discussion.discussionDesc)
                }
            }
        }

        immigrationViewModel.selectedPostingDiscussionScreenCategory.observe(viewLifecycleOwner) {
            if (it != null) {
                discussionCategoryResponseItem = it
                binding?.selectImmigrationCategory?.text = it.discCatValue
            }
        }

        immigrationViewModel.postDiscussionData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        if (response.data?.discussionId != null) {
                            // success
                            findNavController().navigateUp()
                        } else {
                            showAlert("Topic already available")
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
                immigrationViewModel.postDiscussionData.postValue(null)
            }
        }

        immigrationViewModel.updateDiscussionData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        if (response.data?.discussionId != null) {
                            // success
                            findNavController().navigateUp()
                        } else {
                            showAlert("Something went wrong")
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
                immigrationViewModel.updateDiscussionData.postValue(null)
            }
        }

        return binding?.root
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}