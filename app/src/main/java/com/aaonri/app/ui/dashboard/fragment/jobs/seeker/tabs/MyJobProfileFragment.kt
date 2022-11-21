package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.WebViewActivity
import com.aaonri.app.data.jobs.seeker.model.JobProfile
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentMyJobProfileBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobSeekerAdapter
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyJobProfileFragment : Fragment() {
    var binding: FragmentMyJobProfileBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var jobSeekerAdapter: JobSeekerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMyJobProfileBinding.inflate(layoutInflater, container, false)

        jobSeekerAdapter = JobSeekerAdapter()

        jobSeekerAdapter?.itemClickListener = { view, item, position ->
            if (item is JobProfile) {
                if (view.id == R.id.updateProfileBtn) {
                    jobSeekerViewModel.setNavigateToUpdateJobProfileScreen(true, item.id)
                }
            }
        }

        jobSeekerAdapter?.viewResumeOrCoverLetterBtnListener = { isViewCoverLetterClicked, item ->
            if (isViewCoverLetterClicked) {
                jobSeekerViewModel.setUserJobProfileCoverLetterValue(item.coverLetter)
            } else {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(
                    "url",
                    "http://docs.google.com/gview?embedded=true&url=" + BuildConfig.BASE_URL + "/api/v1/common/jobsFile/${item.resumeName}"
                )
                intent.putExtra("hideBottomBar", true)
                startActivity(intent)
            }
        }


        binding?.apply {

            myJobProfileRv.layoutManager = LinearLayoutManager(context)
            myJobProfileRv.adapter = jobSeekerAdapter

            uploadYourProfileBtn.setOnClickListener {
                jobSeekerViewModel.setNavigateToUploadJobProfileScreen(true)
            }

            jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.let {
                            if (it.jobProfile.isNotEmpty()) {
                                itemsNestedScrollView.visibility = View.VISIBLE
                                resultsNotFoundLL.visibility = View.GONE
                                uploadYourProfileBtn.visibility = View.GONE
                                jobSeekerAdapter?.setData(it.jobProfile.subList(0, 1))
                            } else {
                                itemsNestedScrollView.visibility = View.GONE
                                resultsNotFoundLL.visibility = View.VISIBLE
                                uploadYourProfileBtn.visibility = View.VISIBLE
                            }
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
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