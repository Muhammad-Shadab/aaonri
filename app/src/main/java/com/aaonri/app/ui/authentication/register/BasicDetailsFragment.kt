package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentBasicDetailsBinding
import com.aaonri.app.utils.Validation
import com.example.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.xml.validation.Validator

@AndroidEntryPoint
class BasicDetailsFragment : Fragment() {
    var basicDetailsBinding: FragmentBasicDetailsBinding? = null
    val commonViewModel: CommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        basicDetailsBinding = FragmentBasicDetailsBinding.inflate(inflater, container, false)

        var job: Job? = null

        basicDetailsBinding?.apply {


            emailAddressBasicDetails.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                            if (Validation.emailValidation(editable.toString())) {
                                basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                                registrationViewModel.isEmailAlreadyRegister(
                                    EmailVerifyRequest(
                                        emailAddressBasicDetails.text.toString()
                                    )
                                )
                            } else {
                                basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.VISIBLE
                                basicDetailsBinding?.emailAlreadyExistTv?.text =
                                    "Please enter valid email"
                            }
                        } else {
                            basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                        }
                    }
                }
            }


            basicDetailsNextBtn.setOnClickListener {
                val firstName = firstNameBasicDetails.text
                val lastName = lastNameBasicDetails.text
                val emailAddress = emailAddressBasicDetails.text
                val password = passwordBasicDetails.text

                if (firstName?.isNotEmpty() == true && lastName?.isNotEmpty() == true && emailAddress?.isNotEmpty() == true && password?.isNotEmpty() == true) {

                    commonViewModel.addBasicDetails(
                        firstName.toString(),
                        lastName.toString(),
                        emailAddress.toString(),
                        password.toString()
                    )

                    findNavController().navigate(R.id.action_basicDetailsFragment_to_locationDetailsFragment)
                } else {
                    Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registrationViewModel.emailAlreadyRegisterData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                }
                is Resource.Success -> {
                    if (response.data?.status == "true") {
                        basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.VISIBLE
                        basicDetailsBinding?.emailAlreadyExistTv?.text =
                            "This email is already registered"
                    } else {

                    }
                }
                is Resource.Error -> {
                    basicDetailsBinding?.emailAlreadyExistTv?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }


        return basicDetailsBinding?.root
    }

}