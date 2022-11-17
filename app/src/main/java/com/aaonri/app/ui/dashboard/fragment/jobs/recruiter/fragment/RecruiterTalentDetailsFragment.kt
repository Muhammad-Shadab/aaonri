package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.WebViewActivity
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
    var talentEmail = ""
    var talentResume = ""

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

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            viewResumeTv.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                /*intent.putExtra(
                "url",
                "http://docs.google.com/gview?embedded=true&url=" + BuildConfig.BASE_URL+"/api/v1/common/jobsFile/${item.resumeName}"
            )*/
                intent.putExtra(
                    "url",
                    "http://docs.google.com/gview?embedded=true&url=https://www.africau.edu/images/default/sample.pdf"
                )
                intent.putExtra("hideBottomBar", true)
                startActivity(intent)
            }

            connectBtn.setOnClickListener {

                val selectorIntent = Intent(Intent.ACTION_SENDTO)
                selectorIntent.data = Uri.parse("mailto:")

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(talentEmail))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Recruiter wants to connect")
                emailIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Greetings!\n" +
                            "Your Job Profile seems interesting, let's connect for further discussion. Please reply to this email if you are looking for a change."
                )
                emailIntent.selector = selectorIntent

                activity?.startActivity(Intent.createChooser(emailIntent, "Send email..."))

            }

            jobRecruiterViewModel.jobProfileDetailsByIdData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            userNameTv.text = it.firstName + " " + it.lastName
                            roleTitleTv.text = it.title
                            addressTv.text = it.location
                            jobCoverLetterTv.text = Html.fromHtml(it.coverLetter)
                            talentEmail = it.emailId
                            talentResume = it.resumeName
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