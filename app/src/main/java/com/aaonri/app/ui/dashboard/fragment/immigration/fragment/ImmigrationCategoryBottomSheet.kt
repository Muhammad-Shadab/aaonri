package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationCategoryBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImmigrationCategoryBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentImmigrationCategoryBottomSheetBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    val args: ImmigrationCategoryBottomSheetArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        binding =
            FragmentImmigrationCategoryBottomSheetBinding.inflate(layoutInflater, container, false)

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener = { view, item, position ->
            immigrationViewModel.setIsNavigateBackFromImmigrationDetailScreen(false)
            if (item is DiscussionCategoryResponseItem) {
                if (args.isFromAllImiigratonScreen) {
                    immigrationViewModel.setSelectedAllDiscussionCategory(item)
                } else {
                    immigrationViewModel.setSelectedMyDiscussionScreenCategory(item)
                }
                dismiss()
            }
        }

        binding?.apply {

            closeBttomSheetBtn.setOnClickListener {
                dismiss()
            }
            immigrationCategoryRv.layoutManager = LinearLayoutManager(context)
            immigrationCategoryRv.adapter = immigrationAdapter


        }

        immigrationViewModel.discussionCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.let { immigrationAdapter?.setData(it) }
                }
                is Resource.Error -> {

                }
            }
        }

        return binding?.root
    }
}