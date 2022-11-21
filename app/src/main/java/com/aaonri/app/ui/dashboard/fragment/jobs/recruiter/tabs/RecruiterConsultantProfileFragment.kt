package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterConsultantProfileBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.ConsultantProfileAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterConsultantProfileFragment : Fragment() {
    var binding: FragmentRecruiterConsultantProfileBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var consultantProfileAdapter: ConsultantProfileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterConsultantProfileBinding.inflate(inflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        consultantProfileAdapter = ConsultantProfileAdapter()

        binding?.apply {

            recyclerViewConsultantProfile.layoutManager = LinearLayoutManager(context)
            recyclerViewConsultantProfile.adapter = consultantProfileAdapter

            uploadYourProfileBtn.setOnClickListener {
                jobRecruiterViewModel.setNavigateToUploadConsultantProfile(true)
            }

            jobRecruiterViewModel.getUserConsultantProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        if (response.data?.jobProfile?.isNotEmpty() == true) {
                            recyclerViewConsultantProfile.visibility = View.VISIBLE
                            resultsNotFoundLL.visibility = View.GONE
                            uploadYourProfileBtn.visibility = View.GONE
                            jobRecruiterViewModel.setVisibilityToTheFloatingActionBtnValue(
                                hideFloatingBtn = false
                            )
                            response.data.let { consultantProfileAdapter?.setData(it.jobProfile) }
                        } else {
                            resultsNotFoundLL.visibility = View.VISIBLE
                            uploadYourProfileBtn.visibility = View.VISIBLE
                            recyclerViewConsultantProfile.visibility = View.GONE
                            /** Floating action btn will be gone in this screen because there is another already visible for uploading profile**/
                            jobRecruiterViewModel.setVisibilityToTheFloatingActionBtnValue(
                                hideFloatingBtn = true
                            )
                        }
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