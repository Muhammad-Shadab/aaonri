package com.aaonri.app.ui.dashboard.fragment.jobs.post_jobs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aaonri.app.data.jobs.seeker.model.ApplyJobRequest
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobApplyBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobApplyFragment : Fragment() {
    var binding: FragmentJobApplyBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobApplyBinding.inflate(layoutInflater, container, false)

        binding?.apply {


            applyNowBtn.setOnClickListener {
                val phoneNumber = phoneNumberEt.text.toString().replace("-", "")

                if (firstNameEt.text.toString().length >= 3) {
                    if (lastNameEt.text.toString().length >= 3) {
                        if (phoneNumber.length == 10) {
                            if (coverLetterDescEt.text.toString().length >= 3) {
                                /*jobSeekerViewModel.applyJob(
                                    ApplyJobRequest(
                                        coverLetter = ,
                                        email = ,
                                        fullName = ,
                                        jobId = ,
                                        phon
                                    )
                                )*/
                            } else {
                                showAlert("Please enter valid cover Letter Description")
                            }
                        } else {
                            showAlert("Please enter valid Phone Number")
                        }
                    } else {
                        showAlert("Please enter valid Last Name")
                    }
                } else {
                    showAlert("Please enter valid First Name")
                }
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
                                firstNameEt.setText(it[0].firstName)
                                lastNameEt.setText(it[0].lastName)
                                phoneNumberEt.setText(
                                    it[0].phoneNo.replace("""[(,), ]""".toRegex(), "")
                                        .replace("-", "").replaceFirst(
                                            "(\\d{3})(\\d{3})(\\d+)".toRegex(),
                                            "$1-$2-$3"
                                        )
                                )
                                coverLetterDescEt.setText(it[0].coverLetter)
                            }
                        }

                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            phoneNumberEt.addTextChangedListener(object :
                TextWatcher {
                var length_before = 0
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    length_before = s.length
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (length_before < s.length) {
                        if (s.length == 3 || s.length == 7) s.append("-")
                        if (s.length > 3) {
                            if (Character.isDigit(s[3])) s.insert(3, "-")
                        }
                        if (s.length > 7) {
                            if (Character.isDigit(s[7])) s.insert(7, "-")
                        }
                    }
                }
            })
        }

        return binding?.root
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }
}