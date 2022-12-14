package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterJobApplicantsBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.JobApplicantAdapter
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.Resource
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterJobApplicantsFragment : Fragment() {
    var binding: FragmentRecruiterJobApplicantsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var jobApplicantAdapter: JobApplicantAdapter? = null
    val args: RecruiterJobApplicantsFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecruiterJobApplicantsBinding.inflate(inflater, container, false)

        jobApplicantAdapter = JobApplicantAdapter {
            if (it.jobProfileId != null) {
                val action =
                    RecruiterJobApplicantsFragmentDirections.actionRecruiterJobApplicantsFragmentToRecruiterTalentDetailsFragment(
                        it.jobProfileId.toInt()
                    )
                findNavController().navigate(action)
            }

        }

        jobRecruiterViewModel.getJobApplicantList(args.jobId)

        binding?.apply {

            applicantRv.layoutManager = LinearLayoutManager(context)
            applicantRv.adapter = jobApplicantAdapter

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            jobRecruiterViewModel.jobApplicantListData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        response.data?.let {
                            totalApplicantTv.text = "Job Applicants for this job (${it.size})"
                            jobApplicantAdapter?.setData(it)
                        }
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                    }
                }

            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        jobRecruiterViewModel.jobApplicantListData.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}