package com.aaonri.app.ui.dashboard.fragment.immigration.tabs

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.model.ImmigrationFilterModel
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentAllImmigrationBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AllImmigrationFragment : Fragment() {
    var binding: FragmentAllImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    var discussionCategoryResponseItem: DiscussionCategoryResponseItem? = null
    var discussionList = mutableListOf<Discussion>()
    var immigrationFilterModel: ImmigrationFilterModel? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllImmigrationBinding.inflate(layoutInflater, container, false)

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener =
            { view, item, position, updateImmigration, deleteImmigration ->
                if (item is Discussion) {
                    immigrationViewModel.setNavigateFromAllImmigrationToDetailScreen(true)
                    immigrationViewModel.setSelectedDiscussionItem(item)
                }
            }

        binding?.apply {

            selectAllImmigrationSpinner.setOnClickListener {
                immigrationViewModel.setOnAllDiscussionCategoryIsClicked(true)
            }

            allImmigrationRv.layoutManager = LinearLayoutManager(context)
            allImmigrationRv.adapter = immigrationAdapter

            nestedScrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            })

        }

        immigrationViewModel.selectedAllDiscussionScreenCategory.observe(viewLifecycleOwner) {
            discussionCategoryResponseItem = it
            binding?.selectAllImmigrationSpinner?.text = it.discCatValue
            if (immigrationViewModel.callAllImmigrationApi) {
                immigrationViewModel.getAllImmigrationDiscussion(
                    GetAllImmigrationRequest(
                        categoryId = "${it.discCatId}",
                        createdById = "",
                        keywords =
                        ""
                    )
                )
            }
        }

        immigrationViewModel.immigrationSearchQuery.observe(viewLifecycleOwner) {
            if (it != null) {
                immigrationViewModel.getAllImmigrationDiscussion(
                    GetAllImmigrationRequest(
                        categoryId = "${discussionCategoryResponseItem?.discCatId}",
                        createdById = "",
                        keywords = it
                    )
                )
                immigrationViewModel.immigrationSearchQuery.postValue(null)
            }
        }


        immigrationViewModel.allImmigrationDiscussionListData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.discussionList?.let { immigrationAdapter?.setData(it) }
                    if (response.data?.discussionList?.isNotEmpty() == true) {
                        binding?.resultsNotFoundLL?.visibility = View.GONE
                    } else {
                        binding?.resultsNotFoundLL?.visibility = View.VISIBLE
                    }

                    /*response.data?.discussionList?.forEach { discussion ->
                        if (discussion.approved && !activeDiscussionList.contains(discussion)) {
                            activeDiscussionList.add(discussion)
                        }
                    }*/
                    discussionList = response.data?.discussionList as MutableList<Discussion>
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        immigrationViewModel.immigrationFilterData.observe(viewLifecycleOwner) { filterData ->
            Log.i("filterData", "onCreateView: $filterData")
            immigrationFilterModel = filterData
            var filteredList = mutableListOf<Discussion>()

            val previousFilterDays = getCalculatedDate(
                "MM-dd-yyyy",
                if (filterData.fifteenDaysSelected) -15 else if (filterData.threeMonthSelected) -90 else if (filterData.oneYearSelected) -365 else 0
            )

            val currentDate = getCalculatedDate("MM-dd-yyyy", 0)

            discussionList.forEach { discussion ->

                if (filterData.fifteenDaysSelected || filterData.threeMonthSelected || filterData.oneYearSelected) {
                    val apiDate = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                        .format(
                            DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                                .parse(discussion.createdOn)
                        )

                    val d1 = apiDate
                    val d2 = previousFilterDays

                    val sdf = SimpleDateFormat("MM-dd-yyyy")

                    val firstDate: Date = sdf.parse(d1)
                    val secondDate: Date = sdf.parse(d2)
                    val current: Date = sdf.parse(currentDate)

                    val cmp = firstDate.compareTo(secondDate)
                    val cmp1 = firstDate.compareTo(current)
                    when {
                        cmp > 0 && cmp1 < 0 -> {
                            if (!filteredList.contains(discussion)) {
                                filteredList.add(discussion)
                            }
                        }
                        cmp < 0 -> {
                            /*Toast.makeText(context, "before, $apiDate", Toast.LENGTH_SHORT)
                                .show()*/
                        }
                        else -> {
                            print("Both dates are equal")
                        }
                    }

                    if (filterData.atLeastOnDiscussion) {
                        filteredList.filter { discussion.noOfReplies > 0 }
                    }

                }

                if (filterData.atLeastOnDiscussion && !filterData.fifteenDaysSelected && !filterData.threeMonthSelected && !filterData.oneYearSelected) {
                    if (discussion.noOfReplies > 0 && !filteredList.contains(discussion)) {
                        filteredList.add(discussion)
                    }
                }
            }

            if (!filterData.fifteenDaysSelected && !filterData.threeMonthSelected && !filterData.oneYearSelected && !filterData.activeDiscussion && !filterData.atLeastOnDiscussion) {
                filteredList = discussionList
            }

            if (filteredList.isNotEmpty()) {
                binding?.resultsNotFoundLL?.visibility = View.GONE
            } else {
                binding?.resultsNotFoundLL?.visibility = View.VISIBLE
            }
            immigrationAdapter?.setData(filteredList)
        }

        /*immigrationViewModel.keyImmigrationKeyboardListener.observe(viewLifecycleOwner) {
            if (it) {
                immigrationAdapter?.setData(discussionList)
            }
        }*/


        return binding?.root

    }

    fun getCalculatedDate(dateFormat: String?, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}