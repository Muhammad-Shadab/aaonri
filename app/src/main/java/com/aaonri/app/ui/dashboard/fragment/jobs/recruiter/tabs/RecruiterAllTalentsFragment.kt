package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterAllTalentsFragmetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.AllJobProfileAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterAllTalentsFragment : Fragment() {
    var binding: FragmentRecruiterAllTalentsFragmetBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var allJobProfileAdapter: AllJobProfileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruiterAllTalentsFragmetBinding.inflate(inflater, container, false)

        allJobProfileAdapter = AllJobProfileAdapter {
            jobRecruiterViewModel.setNavigateAllJobProfileScreenToTalentProfileDetailsScreen(
                it.id
            )
        }

        binding?.apply {


            recyclerViewAllTalents.layoutManager = LinearLayoutManager(context)
            recyclerViewAllTalents.adapter = allJobProfileAdapter

            jobRecruiterViewModel.allJobProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        response.data?.let { allJobProfileAdapter?.setData(it) }
                        binding?.progressBar?.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        }



        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}