package com.aaonri.app.ui.dashboard.fragment.immigration.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentMyDiscussionImmigrationBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDiscussionImmigrationFragment : Fragment() {
    var binding: FragmentMyDiscussionImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyDiscussionImmigrationBinding.inflate(
            layoutInflater,
            container,
            false
        )

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener = { view, item, position ->
            if (item is Discussion) {
                immigrationViewModel.setNavigateToImmigrationDetailScreen(true)
                immigrationViewModel.setSelectedDiscussionItem(item)
            }
        }

        binding?.apply {

            selectMyImmigrationCategorySpinner.setOnClickListener {
                immigrationViewModel.setOnMyDiscussionCategoryIsClicked(true)
            }

            myImmigrationRv.layoutManager = LinearLayoutManager(context)
            myImmigrationRv.adapter = immigrationAdapter

        }

        immigrationViewModel.selectedMyDiscussionScreenCategory.observe(viewLifecycleOwner) {
            binding?.selectMyImmigrationCategorySpinner?.text = it.discCatValue
            if (!immigrationViewModel.isNavigateBackFromMyImmigrationDetailScreen) {
                immigrationViewModel.getMyImmigrationDiscussion(
                    GetAllImmigrationRequest(
                        categoryId = "${it.discCatId}",
                        createdById = userId.toString(),
                        keywords =
                        ""
                    )
                )
            }
        }

        immigrationViewModel.myImmigrationDiscussionListData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.discussionList?.let { immigrationAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }


        return binding?.root
    }
}