package com.aaonri.app.ui.dashboard.fragment.immigration.tabs

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.immigration.model.DeleteDiscussionRequest
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
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
    var discussionCategoryResponseItem: DiscussionCategoryResponseItem? = null

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

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        Toast.makeText(
            context,
            "onCreate",
            Toast.LENGTH_SHORT
        ).show()

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener =
            { view, item, position, updateImmigration, deleteImmigration ->
                if (item is Discussion) {
                    if (updateImmigration) {
                        if (!item.approved) {
                            immigrationViewModel.setNavigateFromMyImmigrationToUpdateScreen(true)
                            immigrationViewModel.setSelectedDiscussionItem(item)
                        }
                    } else if (deleteImmigration) {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Confirm")
                        builder.setMessage("Are you sure you want to Delete?")
                        builder.setPositiveButton("OK") { dialog, which ->
                            immigrationViewModel.deleteDiscussion(
                                DeleteDiscussionRequest(
                                    discussionId = item.discussionId,
                                    userId = if (email?.isNotEmpty() == true) email else "",
                                )
                            )
                        }
                        builder.setNegativeButton("Cancel") { dialog, which ->

                        }
                        builder.show()
                        /*if (!item.approved) {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Confirm")
                            builder.setMessage("Are you sure you want to Delete?")
                            builder.setPositiveButton("OK") { dialog, which ->
                                immigrationViewModel.deleteDiscussion(
                                    DeleteDiscussionRequest(
                                        discussionId = item.discussionId,
                                        userId = if (email?.isNotEmpty() == true) email else "",
                                    )
                                )
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->

                            }
                            builder.show()
                        }*/
                    } else {
                        immigrationViewModel.setNavigateFromMyImmigrationToDetailScreen(true)
                        immigrationViewModel.setSelectedDiscussionItem(item)
                    }
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
            discussionCategoryResponseItem = it

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
                    if(response.data?.discussionList?.isNotEmpty() == true)
                    {
                        binding?.resultsNotFoundLL?.visibility = View.GONE
                    }
                    else{
                        binding?.resultsNotFoundLL?.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        immigrationViewModel.deleteDiscussionData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    /*immigrationViewModel.getMyImmigrationDiscussion(
                        GetAllImmigrationRequest(
                            categoryId = "${discussionCategoryResponseItem?.discCatId}",
                            createdById = userId.toString(),
                            keywords = ""
                        )
                    )*/

                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }


        return binding?.root
    }
}