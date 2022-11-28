package com.aaonri.app.ui.dashboard.fragment.update_profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentUpdateProfileBinding
import com.aaonri.app.ui.dashboard.fragment.update_profile.adapter.UpdateProfilePagerAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.custom.UserProfileStaticData
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {
    var binding: FragmentUpdateProfileBinding? = null
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    val args: UpdateProfileFragmentArgs by navArgs()

    private val tabTitles =
        arrayListOf("Personal", "Address", "Origin", "Interest")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater, container, false)

        val fragment = this
        val updateProfilePagerAdapter = UpdateProfilePagerAdapter(fragment)
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        binding?.apply {
            updateProfileViewPager.isUserInputEnabled = false

            navigateBack.setOnClickListener {
                if (args.isNavigatingFromJobScreen) {
                    val action =
                        UpdateProfileFragmentDirections.actionUpdateProfileFragmentToHomeScreenFragment()
                    findNavController().navigate(action)
                } else {
                    findNavController().navigateUp()
                }
            }

            updateProfileViewPager.adapter = updateProfilePagerAdapter
            TabLayoutMediator(
                updateProfileTabLayout,
                updateProfileViewPager
            ) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            for (i in 0..3) {
                val textView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.tab_title_text, null) as CardView
                updateProfileTabLayout.getTabAt(i)?.customView =
                    textView
            }
        }

        registrationViewModel.updateUserData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        if (response.data?.user != null) {
                            if (authCommonViewModel.profilePicUri != null) {
                                uploadProfilePicture(
                                    response.data.user.userId,
                                    authCommonViewModel.profilePicUri!!
                                )
                            } else {
                                email?.let { registrationViewModel.findByEmail(email = it) }
                                context?.let { it1 -> PreferenceManager<Int>(it1) }
                                    ?.set("selectedHomeServiceRow", -1)
                                activity?.let { it1 ->
                                    Snackbar.make(
                                        it1.findViewById(android.R.id.content),
                                        "Profile Updated Successfully", Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "Something went wrong", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        registrationViewModel.updateUserData.postValue(null)
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                        Toast.makeText(
                            context,
                            "Error ${response.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

        authCommonViewModel.uploadProfilePicData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        CustomDialog.showLoader(requireActivity())
                    }
                    is Resource.Success -> {
                        CustomDialog.hideLoader()
                        authCommonViewModel.setProfilePicUriValue(null)
                        email?.let { registrationViewModel.findByEmail(email = it) }
                        context?.let { it1 -> PreferenceManager<Int>(it1) }
                            ?.set("selectedHomeServiceRow", -1)
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Profile Updated Successfully", Snackbar.LENGTH_LONG
                            ).show()
                        }
                        authCommonViewModel.uploadProfilePicData.postValue(null)
                    }
                    is Resource.Error -> {
                        CustomDialog.hideLoader()
                        Toast.makeText(
                            context,
                            "Error ${response.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

        registrationViewModel.findByEmailData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    response.data?.let { UserProfileStaticData.setUserProfileDataValue(it) }

                    response.data?.interests?.let {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_INTERESTED_SERVICES, it)
                    }

                    response.data?.profilePic?.let {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(
                                Constant.USER_PROFILE_PIC,
                                "${BuildConfig.BASE_URL}/api/v1/common/profileFile/$it"
                            )
                    }

                    response.data?.isJobRecruiter?.let {
                        context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                            ?.set(Constant.IS_JOB_RECRUITER, it)
                    }

                    response.data?.userId?.let {
                        context?.let { it1 -> PreferenceManager<Int>(it1) }
                            ?.set(Constant.USER_ID, it)
                    }

                    response.data?.city?.let {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_CITY, it)
                    }

                    response.data?.zipcode?.let {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_ZIP_CODE, it)
                    }

                    response.data?.phoneNo?.let {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_PHONE_NUMBER, it)
                    }

                    response.data?.firstName?.let {
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_NAME, "$it ${response.data.lastName}")
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Error ${response.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        authCommonViewModel.setIsUpdateProfile(true)
        authCommonViewModel.setIsNavigatingFromJobScreen(args.isNavigatingFromJobScreen)





        return binding?.root
    }

    private fun uploadProfilePicture(userId: Int?, profilePic: Uri) {

        val file = File(profilePic.toString().replace("file:", ""))

        val id = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage = MultipartBody.Part.createFormData("file", file.name, requestFile)

        authCommonViewModel.uploadProfilePic(requestImage, id)
    }

    override fun onDestroy() {
        super.onDestroy()
        authCommonViewModel.setIsUpdateProfile(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}