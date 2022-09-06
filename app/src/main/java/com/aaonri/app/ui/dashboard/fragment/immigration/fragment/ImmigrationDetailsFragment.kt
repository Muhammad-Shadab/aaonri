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
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationDetailsFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImmigrationDetailsFragment : Fragment() {
    var binding: FragmentImmigrationDetailsFrgamentBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    val args: ImmigrationDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentImmigrationDetailsFrgamentBinding.inflate(layoutInflater, container, false)

        immigrationAdapter = ImmigrationAdapter()

        binding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            allReplyRv.layoutManager = LinearLayoutManager(context)
            allReplyRv.adapter = immigrationAdapter

            immigrationViewModel.selectedDiscussionItem.observe(viewLifecycleOwner) {
                immigrationViewModel.getDiscussionDetailsById(it.discussionId.toString())
                discussionNameTv.text = it.discussionTopic
                postedByTv.text = "Posted by: ${it.createdOn}"
                discussionDesc.text = it.discussionDesc
                noOfReply.text = it.noOfReplies.toString()
                discussionDetailsLl.visibility = View.VISIBLE
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