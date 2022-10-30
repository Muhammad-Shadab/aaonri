package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.CommunityAuth
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentCommunityBottomBinding
import com.aaonri.app.ui.authentication.register.adapter.CommunityItemAdapter
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CommunityBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentCommunityBottomBinding? = null
    val registrationViewModel: RegistrationViewModel by viewModels()
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    private var communityItemAdapter: CommunityItemAdapter? = null
    var selectedCommunitiesSize = 0
    var communityAdapter: CommunityItemAdapter? = null
    var tempArrayList = mutableListOf<CommunityAuth>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        binding = FragmentCommunityBottomBinding.inflate(inflater, container, false)
        getCommunities()
        communityItemAdapter = CommunityItemAdapter { communitiesList ->
            SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
            if (communitiesList.isNotEmpty()) {
                authCommonViewModel.addCommunityList(communitiesList as MutableList<CommunityAuth>)
                binding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
                binding?.numberOfSelectedCommunity?.text =
                    "You have selected ${communitiesList.size + selectedCommunitiesSize} ${if ((communitiesList.size + selectedCommunitiesSize) <= 1) "community" else "communities"}"
            } else {
                authCommonViewModel.addCommunityList(communitiesList as MutableList<CommunityAuth>)
                binding?.numberOfSelectedCommunity?.text =
                    "You have selected 0 community"
            }
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) { selectedCommunitiesList ->
            selectedCommunitiesSize = selectedCommunitiesList.size
            if (selectedCommunitiesList.size == 0) {
                binding?.numberOfSelectedCommunity?.text =
                    "You have selected 0 community"
            } else {
                binding?.numberOfSelectedCommunity?.visibility = View.VISIBLE
                binding?.numberOfSelectedCommunity?.text =
                    "You have selected ${selectedCommunitiesList.size} ${if (selectedCommunitiesList.size <= 1) "community" else "communities"}"
                communityItemAdapter?.setDataSavedList(selectedCommunitiesList)
            }
        }


        binding?.apply {
            closeCommunityBtn.setOnClickListener {
                dismiss()
            }

            nestedScrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            })

            rvBottomFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

            communitySubmitBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            rvBottomFragment.layoutManager = FlexboxLayoutManager(context)
            rvBottomFragment.adapter = communityItemAdapter
        }

        return binding?.root
    }

    private fun getCommunities() {
        authCommonViewModel.getCommunities()
        authCommonViewModel.communitiesList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val sortedList = response.data?.community?.sortedBy { it.communityName }
                    response.data?.community?.let { searchCommunity(it) }
                    sortedList?.let { communityItemAdapter?.setData(it) }
                    /*response.data?.community?.let {
                        sortedList?.let { it1 ->
                            communityItemAdapter?.setData(
                                it1
                            )
                        }
                    }*/

                    /*if (sortedList != null) {
                    }*/
                }
                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                    Log.i("Loading", "Error: ${response.message}")
                }
            }
        }
    }

    private fun searchCommunity(data: List<CommunityAuth>) {
        binding?.searchView?.addTextChangedListener { editable ->
            tempArrayList.clear()
            val searchText = editable.toString().lowercase(Locale.getDefault())
            if (searchText.isNotEmpty()) {
                data.forEach {
                    if (it.communityName?.lowercase(Locale.getDefault())
                            ?.contains(searchText) == true
                    ) {
                        tempArrayList.add(it)
                    }
                }
                communityItemAdapter?.setData(tempArrayList)
                binding?.rvBottomFragment?.adapter?.notifyDataSetChanged()
            } else {
                tempArrayList.clear()
                data.let { tempArrayList.addAll(it) }
                communityItemAdapter?.setData(tempArrayList)
            }
            communityItemAdapter?.setData(tempArrayList)
        }
    }


    // this method Disable drag of BottomSheetDialogFragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}