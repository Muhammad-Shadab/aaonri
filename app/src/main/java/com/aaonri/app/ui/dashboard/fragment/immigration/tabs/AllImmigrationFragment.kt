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
import com.aaonri.app.databinding.FragmentAllImmigrationBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllImmigrationFragment : Fragment() {
    var binding: FragmentAllImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllImmigrationBinding.inflate(layoutInflater, container, false)

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener = { view, item, position ->
            if (item is Discussion) {
                immigrationViewModel.setNavigateToImmigrationDetailScreen(true)
                immigrationViewModel.setSelectedDiscussionItem(item)
            }
        }

        binding?.apply {

            selectAllImmigrationSpinner.setOnClickListener {
                immigrationViewModel.setOnAllDiscussionCategoryIsClicked(true)
            }

            allImmigrationRv.layoutManager = LinearLayoutManager(context)
            allImmigrationRv.adapter = immigrationAdapter

        }

        immigrationViewModel.selectedAllDiscussionScreenCategory.observe(viewLifecycleOwner) {
            binding?.selectAllImmigrationSpinner?.text = it.discCatValue
            immigrationViewModel.getAllImmigrationDiscussion(
                GetAllImmigrationRequest(
                    categoryId = "${it.discCatId}",
                    createdById = "",
                    keywords =
                    ""
                )
            )
        }


        immigrationViewModel.allImmigrationDiscussionListData.observe(viewLifecycleOwner) { response ->
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