package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.immigration.model.Category
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationCenterDetailsBinding


class ImmigrationCenterDetails : Fragment() {
    var binding: FragmentImmigrationCenterDetailsBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var subTitle = mutableListOf<Category>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImmigrationCenterDetailsBinding.inflate(inflater, container, false)
        binding?.apply {
            selectAllImmigrationSpinner.setOnClickListener {
                immigrationViewModel.setImmigrationcenterCategoryIsClicked(true)
            }
            detailsTv.textSize = 16F

            immigrationViewModel.selectedImmigrationCenterItem.observe(viewLifecycleOwner) { it ->
                titleTv.text = it.title
                var category: List<Category> = it.categories
                immigrationViewModel.setImmigrationCenterDesc(it.categories[0])
                category.forEach {
                    if (!subTitle.contains(it)) {
                        subTitle.add(it)
                    }
                }
                immigrationViewModel.setImmigrationList(subTitle)
            }

            immigrationViewModel.immigrationCenterDesc.observe(viewLifecycleOwner) {
                detailsTv.fromHtml(it.description)
                subtitleTv.text = Html.fromHtml(it.title)
                selectAllImmigrationSpinner.text = Html.fromHtml(it.title)
            }

            immigrationViewModel.ImmigrationCenterCategoryIsClicked.observe(viewLifecycleOwner) {
                if (it) {
                    val action =
                        ImmigrationCenterDetailsDirections.actionImmigrationCenterDetailsToImmigrationInfoCenterBottomSheet()
                    findNavController().navigate(action)
                    immigrationViewModel.setImmigrationcenterCategoryIsClicked(false)

                }
            }
            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding?.root
    }


}