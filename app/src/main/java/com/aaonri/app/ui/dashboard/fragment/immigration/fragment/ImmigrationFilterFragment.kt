package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationFilterBinding

class ImmigrationFilterFragment : Fragment() {
    var binding: FragmentImmigrationFilterBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImmigrationFilterBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            selectCategoryClassifiedSpinner.setOnClickListener {
                val action =
                    ImmigrationFilterFragmentDirections.actionImmigrationFilterFragmentToImmigrationCategoryBottomSheet(
                        "FromFilterScreen"
                    )
                findNavController().navigate(action)
            }

            closeBtn.setOnClickListener {
                activity?.onBackPressed()
            }

            applyBtn.setOnClickListener {
                activity?.onBackPressed()
            }

            clearAllBtn.setOnClickListener {

            }

        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    immigrationViewModel.setIsNavigateBackFromImmigrationDetailScreen(true)
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }

}