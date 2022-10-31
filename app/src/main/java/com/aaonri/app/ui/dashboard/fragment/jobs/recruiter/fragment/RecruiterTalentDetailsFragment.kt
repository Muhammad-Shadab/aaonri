package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterTalentDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.RecruiterJobKeySkillsAdapter
import com.aaonri.app.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterTalentDetailsFragment : Fragment() {
    var binding: FragmentRecruiterTalentDetailsBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    val args: RecruiterTalentDetailsFragmentArgs by navArgs()
    var recruiterJobKeySkillsAdapter: RecruiterJobKeySkillsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruiterTalentDetailsBinding.inflate(inflater, container, false)

        jobRecruiterViewModel.findJobProfileById(args.profileId)

        recruiterJobKeySkillsAdapter = RecruiterJobKeySkillsAdapter()

        binding?.apply {


            jobKeySkillsRv.layoutManager = FlexboxLayoutManager(context)
            jobKeySkillsRv.adapter = recruiterJobKeySkillsAdapter

            jobRecruiterViewModel.jobProfileDetailsByIdData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            userNameTv.text = it.firstName + " " + it.lastName
                            jobRoleTv.text = it.coverLetter
                            addressTv.text = it.location
                            //moneyTv.text = it.
                            experienceTv.text = it.experience
                            //jobCategoriesTv.text = it.

                            recruiterJobKeySkillsAdapter?.setData(
                                it.skillSet.split(",").toTypedArray().toList()
                            )

                            linearLayout.visibility = View.VISIBLE
                            connectBtn.visibility = View.VISIBLE


                        }

                        progressBar.visibility = View.GONE

                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }

            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        jobRecruiterViewModel.jobProfileDetailsByIdData.postValue(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}