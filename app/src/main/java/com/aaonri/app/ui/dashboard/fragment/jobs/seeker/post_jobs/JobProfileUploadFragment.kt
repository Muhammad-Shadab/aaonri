package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.post_jobs

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentUploadJobProfileBinding
import com.aaonri.app.ui.dashboard.RichTextEditorActivity
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.SelectedVisaStatusJobSeeker
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.Validator
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class JobProfileUploadFragment : Fragment() {
    var binding: FragmentUploadJobProfileBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var selectedVisaStatusJobSeeker: SelectedVisaStatusJobSeeker? = null
    val args: JobProfileUploadFragmentArgs by navArgs()
    var jobDetailsApplicabilityList = mutableListOf<String>()
    var fileName: String? = null
    var description = ""
    var visaStatus = ""

    /**Getting rich text content**/
    private val resultLauncherEditText =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    binding?.coverLetterDescEt?.fromHtml(data.trim())
                    description = data.trim()
                } else {
                    binding?.coverLetterDescEt?.text = ""
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadJobProfileBinding.inflate(layoutInflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        /**Getting files from file manager**/
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    if (data != null) {
                        val fileUri = data.data
                        fileName = context?.let { data.data?.getName(it) }

                        val returnCursor =
                            fileUri?.let {
                                context?.contentResolver?.query(
                                    it,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            }
                        val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor?.moveToFirst();

                        val size = sizeIndex?.let { returnCursor.getInt(it) }?.div(1000);

                        returnCursor?.close()

                        if (size != null) {
                            if (size <= 5120) {
                                visibleResumeFile()
                                jobSeekerViewModel.setResumeFileUriValue(fileUri)
                            } else {
                                showAlert("File size must be less then 5 MB")
                            }
                        }
                    }
                }
            }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/*"
                resultLauncher.launch(intent)
            } else {
                showAlert("Please allow storage permission")
            }
        }

        selectedVisaStatusJobSeeker = SelectedVisaStatusJobSeeker {
            jobSeekerViewModel.setSelectedVisaStatusJobApplicabilityValue(it)
        }

        binding?.apply {

            coverLetterDescEt.textSize = 14F

            visaStatusRv.layoutManager = FlexboxLayoutManager(context)
            visaStatusRv.adapter = selectedVisaStatusJobSeeker

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            selectExperienceTv.setOnClickListener {
                val action =
                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobGenericBottomSheet(
                        "experienceSelection"
                    )
                findNavController().navigate(action)
            }

            visaStatusCl.setOnClickListener {
                val action =
                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobSeekerSelectVisaStatusBottomSheet()
                findNavController().navigate(action)
            }

            selectAvailabilityTv.setOnClickListener {
                val action =
                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobGenericBottomSheet(
                        "availabilitySelection"
                    )
                findNavController().navigate(action)
            }

            uploadResumeBtnLl.setOnClickListener {
                if (checkPermission()) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "application/*"
                    resultLauncher.launch(intent)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            deleteResume.setOnClickListener {
                binding?.uploadResumeLl?.gravity = Gravity.CENTER
                binding?.uploadResumeBtnLl?.visibility = View.VISIBLE
                binding?.uploadedResumeShapeLl?.visibility = View.GONE
                jobSeekerViewModel.setResumeFileUriValue("".toUri())
                fileName = ""
            }

            coverLetterDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditorActivity::class.java)
                intent.putExtra("isFromAdvertiseBasicDetails", false)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Cover Letter*")
                resultLauncherEditText.launch(intent)
            }

            uploadProfileNextBtn.setOnClickListener {

                val phoneNumber = phoneNumberEt.text.toString().replace("-", "")

                if (firstNameEt.text.toString().length >= 3) {
                    if (lastNameEt.text.toString().length >= 3) {
                        if (currentTitleEt.text.toString().length >= 3) {
                            if (Validator.emailValidation(contactEmailEt.text.toString().trim())) {
                                if (phoneNumber.length == 10) {
                                    if (locationEt.text.toString().length >= 3) {
                                        if (selectExperienceTv.text.toString().isNotEmpty()) {
                                            if (visaStatus.isNotEmpty()) {
                                                if (selectAvailabilityTv.text.toString()
                                                        .isNotEmpty()
                                                ) {
                                                    if (skillSetDescEt.text.toString().length >= 3) {
                                                        if (coverLetterDescEt.text.toString()
                                                                .trim().length >= 3
                                                        ) {
                                                            if (jobSeekerViewModel.resumeFileUri != null
                                                            ) {
                                                                if (jobSeekerViewModel.resumeFileUri.toString()
                                                                        .isNotEmpty() || fileName?.isNotEmpty() == true
                                                                ) {
                                                                    if (args.isUpdateProfile) {
                                                                        jobSeekerViewModel.updateJobProfile(
                                                                            profileId = args.jobId,
                                                                            addJobProfileRequest = AddJobProfileRequest(
                                                                                availability = selectAvailabilityTv.text.toString(),
                                                                                contactEmailId = contactEmailEt.text.toString(),
                                                                                coverLetter = if (description.isNotEmpty()) description else coverLetterDescEt.text.toString()
                                                                                    .trim(),
                                                                                emailId = email
                                                                                    ?: "",
                                                                                experience = selectExperienceTv.text.toString(),
                                                                                firstName = firstNameEt.text.toString(),
                                                                                isActive = true,
                                                                                isApplicant = true,
                                                                                lastName = lastNameEt.text.toString(),
                                                                                location = locationEt.text.toString(),
                                                                                phoneNo = phoneNumber,
                                                                                resumeName = fileName
                                                                                    ?: "",
                                                                                skillSet = skillSetDescEt.text.toString(),
                                                                                title = currentTitleEt.text.toString(),
                                                                                visaStatus = visaStatus,
                                                                                profileImage = "${
                                                                                    profile?.replace(
                                                                                        "${BuildConfig.BASE_URL}/api/v1/common/profileFile/",
                                                                                        ""
                                                                                    )
                                                                                }"
                                                                            )
                                                                        )
                                                                    } else {
                                                                        jobSeekerViewModel.addJobProfile(
                                                                            AddJobProfileRequest(
                                                                                availability = selectAvailabilityTv.text.toString(),
                                                                                contactEmailId = contactEmailEt.text.toString(),
                                                                                coverLetter = if (description.isNotEmpty()) description else coverLetterDescEt.text.toString()
                                                                                    .trim(),
                                                                                emailId = email
                                                                                    ?: "",
                                                                                experience = selectExperienceTv.text.toString(),
                                                                                firstName = firstNameEt.text.toString(),
                                                                                isActive = true,
                                                                                isApplicant = true,
                                                                                lastName = lastNameEt.text.toString(),
                                                                                location = locationEt.text.toString(),
                                                                                phoneNo = phoneNumber,
                                                                                resumeName = fileName
                                                                                    ?: "",
                                                                                skillSet = skillSetDescEt.text.toString(),
                                                                                title = currentTitleEt.text.toString(),
                                                                                visaStatus = visaStatus,
                                                                                profileImage = "${
                                                                                    profile?.replace(
                                                                                        "${BuildConfig.BASE_URL}/api/v1/common/profileFile/",
                                                                                        ""
                                                                                    )
                                                                                }"
                                                                            )
                                                                        )
                                                                    }
                                                                } else {
                                                                    showAlert("Please upload Resume")
                                                                }
                                                            } else {
                                                                showAlert("Please upload Resume")
                                                            }
                                                        } else {
                                                            showAlert("Please enter valid Cover Letter Description")
                                                        }
                                                    } else {
                                                        showAlert("Please enter valid Skill Set")
                                                    }
                                                } else {
                                                    showAlert("Please choose valid Availability")
                                                }
                                            } else {
                                                showAlert("Please choose valid Visa Status")
                                            }
                                        } else {
                                            showAlert("Please choose valid Experience")
                                        }
                                    } else {
                                        showAlert("Please enter valid Location")
                                    }
                                } else {
                                    showAlert("Please enter valid Phone Number")
                                }
                            } else {
                                showAlert("Please enter valid Email")
                            }
                        } else {
                            showAlert("Please enter valid Title")
                        }
                    } else {
                        showAlert("Please enter valid Last Name")
                    }
                } else {
                    showAlert("Please enter valid First Name")
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

            jobSeekerViewModel.getUserJobProfileData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.let {
                            if (it.jobProfile.isNotEmpty()) {
                                jobDetailsApplicabilityList =
                                    it.jobProfile[0].visaStatus.split(",").toTypedArray()
                                        .toMutableList()
                                fileName = it.jobProfile[0].resumeName
                                firstNameEt.setText(it.jobProfile[0].firstName)
                                lastNameEt.setText(it.jobProfile[0].lastName)
                                currentTitleEt.setText(it.jobProfile[0].title)
                                contactEmailEt.setText(it.jobProfile[0].contactEmailId)
                                phoneNumberEt.setText(it.jobProfile[0].phoneNo)
                                locationEt.setText(it.jobProfile[0].location)
                                selectExperienceTv.text = it.jobProfile[0].experience
                                selectAvailabilityTv.text = it.jobProfile[0].availability
                                skillSetDescEt.setText(it.jobProfile[0].skillSet)
                                coverLetterDescEt.text = Html.fromHtml(it.jobProfile[0].coverLetter)
                                description = it.jobProfile[0].coverLetter
                                if (fileName?.isNotEmpty() == true) {
                                    visibleResumeFile()
                                    binding?.appbarTextTv?.text = "Update Profile"
                                    binding?.uploadProfileNextBtn?.text = "UPDATE"
                                }

                            }
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            jobSeekerViewModel.allActiveJobApplicabilityData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        response.data?.forEachIndexed { index, visaStatus ->
                            response.data[index].isSelected =
                                jobDetailsApplicabilityList.indexOfFirst { it == visaStatus.applicability } != -1
                        }

                        response.data?.let {
                            jobSeekerViewModel.setSelectedVisaStatusJobApplicabilityValue(
                                it
                            )
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            jobSeekerViewModel.selectedVisaStatusJobApplicability.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    selectedVisaStatusJobSeeker?.setData(it)
                    val index = it.indexOfFirst { it.isSelected }
                    if (index == -1) {
                        visaStatus = ""
                        selectVisaStatusTv.visibility = View.VISIBLE
                        visaStatusRv.visibility = View.GONE
                    } else {
                        selectVisaStatusTv.visibility = View.GONE
                        visaStatusRv.visibility = View.VISIBLE
                    }

                    visaStatus = ""
                    it.forEach { item ->
                        if (item.isSelected) {
                            if (!visaStatus.contains(item.applicability)) {
                                visaStatus += item.applicability + ","
                            }
                        }
                    }
                    visaStatus.dropLast(1)
                }
            }
        }

        jobSeekerViewModel.getAllActiveJobApplicability()
        jobSeekerViewModel.getAllActiveAvailability()

        jobSeekerViewModel.selectedExperienceLevel.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.selectExperienceTv?.text = it.experienceLevel
            }
        }
        jobSeekerViewModel.selectedJobApplicability.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.selectVisaStatusTv?.text = it.applicability
            }
        }
        jobSeekerViewModel.selectedJobAvailability.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.selectAvailabilityTv?.text = it.availability
            }
        }

        jobSeekerViewModel.addJobProfileData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        callUploadResumeApi(response.data?.id)
                        jobSeekerViewModel.addJobProfileData.postValue(null)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        }

        jobSeekerViewModel.updateJobProfileData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE

                        if (jobSeekerViewModel.resumeFileUri != null) {
                            if (jobSeekerViewModel.resumeFileUri.toString().trim().isNotEmpty()) {
                                callUploadResumeApi(response.data?.id)
                            } else {
                                val action =
                                    JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobProfileUploadSuccessFragment(
                                        "UpdateProfileScreen", false
                                    )
                                findNavController().navigate(action)
                            }
                        } else {
                            val action =
                                JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobProfileUploadSuccessFragment(
                                    "UpdateProfileScreen", false
                                )
                            findNavController().navigate(action)
                        }
                        jobSeekerViewModel.updateJobProfileData.postValue(null)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        }

        jobSeekerViewModel.uploadResumeData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val action =
                            JobProfileUploadFragmentDirections.actionJobProfileUploadFragmentToJobProfileUploadSuccessFragment(
                                "ProfileUploadScreen", false
                            )
                        findNavController().navigate(action)
                        jobSeekerViewModel.uploadResumeData.postValue(null)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        }

        /*if (args.isUpdateProfile) {

        }*/

        return binding?.root
    }

    private fun visibleResumeFile() {
        binding?.uploadResumeLl?.gravity = Gravity.START
        binding?.uploadResumeBtnLl?.visibility = View.GONE
        binding?.uploadedResumeShapeLl?.visibility = View.VISIBLE
        binding?.resumeNameTv?.text = fileName

        /*if (enableGrayColor) {
            binding?.uploadedResumeShapeLl?.backgroundTintList = ColorStateList.valueOf(
                resources.getColor(
                    R.color.darkGrayColor
                )
            )
        } else {
            binding?.uploadedResumeShapeLl?.backgroundTintList = ColorStateList.valueOf(
                resources.getColor(
                    R.color.blueBtnColor
                )
            )
        }*/
    }

    private fun callUploadResumeApi(id: Int?) {

        val file = createTmpFileFromUri(jobSeekerViewModel.resumeFileUri)

        val requestFile: RequestBody? =
            file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage =
            requestFile?.let { MultipartBody.Part.createFormData("file", file?.name, it) }

        requestImage?.let { jobSeekerViewModel.uploadResume(id ?: 0, true, it) }
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun checkPermission(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    fun Uri.getName(context: Context): String? {
        val returnCursor = context.contentResolver.query(this, null, null, null, null)
        val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor?.moveToFirst()
        val fileName = nameIndex?.let { returnCursor.getString(it) }
        returnCursor?.close()
        return fileName
    }

    /*private fun getFilePath(): String? {
        val tempId = DocumentsContract.getDocumentId(jobSeekerViewModel.resumeFileUri)
        if (!TextUtils.isEmpty(tempId)) {
            if (tempId.startsWith("raw:")) {
                return tempId.replaceFirst("raw:", "");
            }
            try {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), tempId.toLong()
                )
                return getDataColumn(context, contentUri, null, null);
            } catch (e: NumberFormatException) {
                Log.e(
                    "FileUtils",
                    "Downloads provider returned unexpected uri " + jobSeekerViewModel.resumeFileUri.toString(),
                    e
                )
            }
        }
        return null
    }*/

    override fun onDestroy() {
        super.onDestroy()
        jobSeekerViewModel.selectedExperienceLevel.postValue(null)
        jobSeekerViewModel.selectedJobApplicability.postValue(null)
        jobSeekerViewModel.selectedJobAvailability.postValue(null)
        jobSeekerViewModel.setResumeFileUriValue("".toUri())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun createTmpFileFromUri(uri: Uri?): File? {
        return try {
            val stream = uri?.let { context?.contentResolver?.openInputStream(it) }
            val file = File.createTempFile(fileName, "", context?.cacheDir)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}