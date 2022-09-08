package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.Category
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationInfoCenterBottomSheetBinding
import com.aaonri.app.databinding.FragmentInformationCenterImmigrationBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImmigrationInfoCenterBottomSheet : BottomSheetDialogFragment() {
    var binding:FragmentImmigrationInfoCenterBottomSheetBinding? = null
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    val args: ImmigrationCategoryBottomSheetArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImmigrationInfoCenterBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )


        immigrationAdapter = ImmigrationAdapter()
        immigrationAdapter?.itemClickListener =
            { _, item, _, _, _ ->
                if (item is Category) {
                    immigrationViewModel.setImmigrationCenterDesc(item)
                    dismiss()
                }
            }
        binding?.apply {
            immigrationCategoryRv.layoutManager = LinearLayoutManager(context)
            immigrationCategoryRv.adapter = immigrationAdapter

           immigrationViewModel.immigrationCenterList.observe(viewLifecycleOwner){
               immigrationAdapter?.setData(it)

           }
            closeBttomSheetBtn.setOnClickListener {
                dismiss()
            }
        }
        return  binding?.root
    }

}