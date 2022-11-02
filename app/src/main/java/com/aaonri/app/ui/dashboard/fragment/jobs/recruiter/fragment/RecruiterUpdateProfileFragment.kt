package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import com.aaonri.app.databinding.FragmentRecruiterUpdateProfileBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecruiterUpdateProfileFragment : Fragment() {
    var binding: FragmentRecruiterUpdateProfileBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var isUpdateConsultantProfile = false
    var consultantProfileId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruiterUpdateProfileBinding.inflate(inflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        binding?.apply {

            val phoneNumber = contactEmailEt.text.toString().trim().replace("-", "")

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            submitBtn.setOnClickListener {
                if (firstNameEt.text.toString().length >= 3) {
                    if (lastNameEt.text.toString().length >= 3) {
                        if (Validator.emailValidation(contactEmailEt.text.toString().trim())) {
                            if (phoneNumber.length == 10) {
                                if (locationEt.text.toString().length >= 3) {

                                    if (isUpdateConsultantProfile) {
                                        jobRecruiterViewModel.updateConsultantProfile(
                                            consultantProfileId,
                                            AddJobProfileRequest(
                                                availability = "",
                                                contactEmailId = contactEmailEt.text.toString()
                                                    .trim(),
                                                coverLetter = "",
                                                emailId = "$email",
                                                experience = "",
                                                firstName = firstNameEt.text.toString(),
                                                isActive = true,
                                                isApplicant = false,
                                                lastName = lastNameEt.text.toString(),
                                                location = locationEt.text.toString(),
                                                phoneNo = phoneNumberEt.text.toString(),
                                                resumeName = "",
                                                skillSet = "",
                                                title = "",
                                                visaStatus = ""
                                            )
                                        )
                                    } else {
                                        jobRecruiterViewModel.addConsultantProfile(
                                            AddJobProfileRequest(
                                                availability = "",
                                                contactEmailId = contactEmailEt.text.toString()
                                                    .trim(),
                                                coverLetter = "",
                                                emailId = "$email",
                                                experience = "",
                                                firstName = firstNameEt.text.toString(),
                                                isActive = true,
                                                isApplicant = false,
                                                lastName = lastNameEt.text.toString(),
                                                location = locationEt.text.toString(),
                                                phoneNo = phoneNumberEt.text.toString(),
                                                resumeName = "",
                                                skillSet = "",
                                                title = "",
                                                visaStatus = ""
                                            )
                                        )
                                    }
                                } else {
                                    showAlert("Please enter valid location")
                                }
                            } else {
                                showAlert("Please enter valid phone number")
                            }
                        } else {
                            showAlert("Please enter valid contact email")
                        }
                    } else {
                        showAlert("Please enter valid last name")
                    }
                } else {
                    showAlert("Please enter valid first name")
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

            jobRecruiterViewModel.getUserConsultantProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        if (response.data?.isNotEmpty() == true) {
                            isUpdateConsultantProfile = true
                            response.data[0].let {
                                consultantProfileId = it.id
                                firstNameEt.setText(it.firstName)
                                lastNameEt.setText(it.lastName)
                                contactEmailEt.setText(it.contactEmailId)
                                phoneNumberEt.setText(
                                    it.phoneNo.replace("""[(,), ]""".toRegex(), "")
                                        .replace("-", "").replaceFirst(
                                            "(\\d{3})(\\d{3})(\\d+)".toRegex(),
                                            "$1-$2-$3"
                                        )
                                )
                                locationEt.setText(it.location)
                            }
                        } else {
                            isUpdateConsultantProfile = false
                        }

                        binding?.progressBar?.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }

            jobRecruiterViewModel.addConsultantProfileData.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            findNavController().navigateUp()
                            jobRecruiterViewModel.addConsultantProfileData.postValue(null)
                        }
                        is Resource.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                        }
                    }
                }
            }

            jobRecruiterViewModel.updateConsultantProfileData.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            findNavController().navigateUp()
                            jobRecruiterViewModel.updateConsultantProfileData.postValue(null)
                        }
                        is Resource.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                        }
                    }
                }
            }

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