package com.aaonri.app.ui.dashboard.fragment.jobs.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentMyJobProfileBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.adapter.JobSeekerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
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
                            if (it.size > 0) {
                                itemsNestedScrollView.visibility = View.VISIBLE
                                resultsNotFoundLL.visibility = View.GONE
                                uploadYourProfileBtn.visibility = View.GONE
                                jobSeekerAdapter?.setData(it)
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}