package com.aaonri.app.ui.dashboard.fragment.immigration.post_immigration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentPostImmigrationBinding

class PostImmigrationFragment : Fragment() {
    var binding: FragmentPostImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostImmigrationBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            navigateBack.setOnClickListener {
                activity?.onBackPressed()
            }



        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(true)
                    immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(true)
                    findNavController().navigateUp()
                }
            })

        return binding?.root
    }
}