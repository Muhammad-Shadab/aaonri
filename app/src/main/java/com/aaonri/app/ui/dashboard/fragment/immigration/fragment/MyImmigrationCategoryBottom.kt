package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentMyImmigrationCategoryBottomBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyImmigrationCategoryBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentMyImmigrationCategoryBottomBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        // Inflate the layout for this fragment
        binding =
            FragmentMyImmigrationCategoryBottomBinding.inflate(layoutInflater, container, false)

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener = { view, item, position ->
            if (item is DiscussionCategoryResponseItem) {
                immigrationViewModel.setSelectedMyDiscussionScreenCategory(item)
                dismiss()
            }
        }


        return binding?.root

    }
}