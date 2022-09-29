package com.aaonri.app.ui.dashboard.fragment.jobs.seeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobScreenBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobPagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobScreenFragment : Fragment() {
    var binding: FragmentJobScreenBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    private val tabTitles =
        arrayListOf("All Jobs", "Job Alerts", "My Profile")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobScreenBinding.inflate(inflater, container, false)

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val isJobRecruiter =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_JOB_RECRUITER, false] }

        val fragment = this
        val jobPagerAdapter = JobPagerAdapter(fragment)

        jobSeekerViewModel.getUserJobProfileByEmail(
            emailId = email ?: "",
            isApplicant = isJobRecruiter != true
        )

        binding?.apply {

            context?.let { Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).centerCrop().error(R.drawable.profile_pic_placeholder).into(profilePicIv) }

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            jobScreenViewPager.adapter = jobPagerAdapter
            TabLayoutMediator(
                jobsScreenTabLayout,
                jobScreenViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                jobsScreenTabLayout.getTabAt(i)?.customView =
                    textView
            }

            jobsScreenTabLayout.getTabAt(1)?.view?.isClickable = false

            jobsScreenTabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 2) {

                    } else {

                    }
                    if (tab?.position != 0) {

                    } else {

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }
            })
            jobScreenViewPager.isUserInputEnabled = false
        }

        jobSeekerViewModel.navigateAllJobToDetailsJobScreen.observe(viewLifecycleOwner) { jobId ->
            if (jobId != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobDetailsFragment(jobId)
                findNavController().navigate(action)
                jobSeekerViewModel.navigateAllJobToDetailsJobScreen.postValue(null)
            }
        }

        jobSeekerViewModel.navigateToUploadJobProfileScreen.observe(viewLifecycleOwner) {
            if (it != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobProfileUploadFragment(
                        false,
                        0
                    )
                findNavController().navigate(action)
                jobSeekerViewModel.navigateToUploadJobProfileScreen.postValue(null)
            }
        }

        jobSeekerViewModel.navigateToUpdateJobProfileScreen.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                val action =
                    JobScreenFragmentDirections.actionJobScreenFragmentToJobProfileUploadFragment(
                        true,
                        pair.second
                    )
                findNavController().navigate(action)
                jobSeekerViewModel.navigateToUpdateJobProfileScreen.postValue(null)
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}