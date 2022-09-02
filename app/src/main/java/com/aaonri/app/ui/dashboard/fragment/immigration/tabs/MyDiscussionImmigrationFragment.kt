package com.aaonri.app.ui.dashboard.fragment.immigration.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentMyDiscussionImmigrationBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDiscussionImmigrationFragment : Fragment() {
    var binding: FragmentMyDiscussionImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyDiscussionImmigrationBinding.inflate(
            layoutInflater,
            container,
            false
        )

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener = { view, item, position ->

        }

        binding?.apply {

            selectMyImmigrationSpinner.setOnClickListener {
                immigrationViewModel.setOnMyDiscussionCategoryIsClicked(true)
            }

        }


        return binding?.root
    }
}