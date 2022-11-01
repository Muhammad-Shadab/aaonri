package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentRecruiterMyPostedJobBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.MyPostedJobAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job.RecruiterPostJobActivity
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterMyPostedJobFragment : Fragment() {
    var binding: FragmentRecruiterMyPostedJobBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var myPostedJobAdapter: MyPostedJobAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruiterMyPostedJobBinding.inflate(inflater, container, false)

        myPostedJobAdapter =
            MyPostedJobAdapter { isEditBtnClicked, isActivateBtnClicked, isDeactivateBtnClicked, isJobCardClicked, value ->
                if (isEditBtnClicked) {
                    val intent = Intent(context, RecruiterPostJobActivity::class.java)
                    activity?.startActivity(intent)
                } else if (isActivateBtnClicked) {

                } else if (isDeactivateBtnClicked) {

                } else if (isJobCardClicked) {
                    jobRecruiterViewModel.setNavigateFromMyPostedJobToJobDetailsScreen(value.jobId)
                }
            }

        binding?.apply {

            recyclerViewMyPostedJob.layoutManager = LinearLayoutManager(context)
            recyclerViewMyPostedJob.adapter = myPostedJobAdapter

            jobRecruiterViewModel.jobSearchData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {

                        progressBar.visibility = View.GONE
                        response.data?.jobDetailsList?.let { myPostedJobAdapter?.setData(it) }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }

        return binding?.root
    }


}