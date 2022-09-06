package com.aaonri.app.ui.dashboard.fragment.immigration.post_immigration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.PostDiscussionRequest
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
                            immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(
                                false
                            )
                            immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(
                                false
                            )
                            immigrationViewModel.postDiscussion(
                                PostDiscussionRequest(
                                    discCatId = if (discussionCategoryResponseItem?.discCatId != null) discussionCategoryResponseItem!!.discCatId else 0,
                                    discussionDesc = descEt.text.toString(),
                                    discussionTopic = discussionTopicEt.text.toString(),
                                    userId = email ?: ""
                                )
                            )
                            findNavController().navigateUp()
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

        }

        immigrationViewModel.selectedPostingDiscussionScreenCategory.observe(viewLifecycleOwner) {
            discussionCategoryResponseItem = it
            binding?.selectImmigrationCategory?.text = it.discCatValue
        }

        immigrationViewModel.postDiscussionData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    if (response.data?.discussionId != null) {
                        // success
                    } else {
                        showAlert("Topic already available")
                    }
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
                    immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(true)
                    immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(true)
                    findNavController().navigateUp()
                }
            })

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
}