package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.data.jobs.seeker.model.SaveJobViewRequest
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class JobDetailsFragment : Fragment() {
    var binding: FragmentJobDetailsBinding? = null
    val args: JobDetailsFragmentArgs by navArgs()
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var jobId = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }

        binding?.apply {

            jobDescTv.textSize = 14F

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            jobSeekerViewModel.getJobDetails(args.jobId)
            jobSeekerViewModel.saveJobView(
                SaveJobViewRequest(
                    id = userId ?: 0,
                    jobId = args.jobId,
                    isApplied = false,
                    lastViewedOn = "",
                    noOfTimesViewed = 0,
                    userId = email ?: "",
                    viewerName = "",
                )
            )

            applyBtn.setOnClickListener {
                navigateToJobApplyScreen()
            }

            jobSeekerViewModel.jobDetailData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        var applicability = ""
                        progressBar.visibility = View.GONE
                        response.data?.let {

                            val random =
                                if (it.salaryRange != "string") it.salaryRange.toDouble() else 0
                            val df = DecimalFormat("#,###.00")
                            df.roundingMode = RoundingMode.DOWN
                            val roundoff = df.format(random)

                            jobId = it.jobId
                            jobNameTv.text = it.title
                            companyNameTv.text = it.company
                            experienceTv.text = it.experienceLevel
                            addressTv.text = "${it.country}, ${it.state}, ${it.city}"
                            moneyTv.text = roundoff
                            jobCategoriesTv.text = it.jobType
                            dateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                                .format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        .parse(it.createdOn.split("T")[0])
                                )

                            jobViewTv.text = it.viewCount.toString()
                            jobApplicationTv.text = it.applyCount.toString()
                            jobDescTv.fromHtml(it.description)
                            jobKeySkillsTv.text = it.skillSet.replace(",", " \u2022")
                            it.applicability.forEach { apl ->
                                applicability += "${apl.applicability}, "
                            }
                            jobRequirementTv.text = applicability
                        }
                        linearLayout.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

        }


        return binding?.root
    }

    private fun navigateToJobApplyScreen() {
        jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    response.data?.let {
                        if (it.size > 0) {
                            /** Profile Uploaded **/
                            val action =
                                JobDetailsFragmentDirections.actionJobDetailsFragmentToJobApplyFragment(
                                    jobId
                                )
                            findNavController().navigate(action)
                        } else {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Confirm")
                            builder.setMessage("Seems you haven't uploaded your job profile yet.")
                            builder.setPositiveButton("Upload Profile") { dialog, which ->
                                val action =
                                    JobDetailsFragmentDirections.actionJobDetailsFragmentToJobProfileUploadFragment(
                                        false,
                                        0
                                    )
                                findNavController().navigate(action)
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->

                            }
                            builder.show()
                        }
                    }

                }
                is Resource.Error -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        jobSeekerViewModel.jobDetailData.postValue(null)
    }


}