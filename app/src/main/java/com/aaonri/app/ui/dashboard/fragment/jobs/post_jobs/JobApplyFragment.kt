package com.aaonri.app.ui.dashboard.fragment.jobs.post_jobs

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
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
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.ApplyJobRequest
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.databinding.FragmentJobApplyBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class JobApplyFragment : Fragment() {
    var binding: FragmentJobApplyBinding? = null
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()
    var fileName: String? = null
    val args: JobApplyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobApplyBinding.inflate(layoutInflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        fileName = "${email}.pdf"

        if (fileName?.isNotEmpty() == true) {
            visibleResumeFile(true)
        }

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

        binding?.apply {

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            uploadResumeTv.setOnClickListener {
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
                binding?.uploadResumeTv?.visibility = View.VISIBLE
                binding?.sizeLimitTv?.visibility = View.VISIBLE

                binding?.uploadedResumeShapeLl?.visibility = View.GONE
                jobSeekerViewModel.setResumeFileUriValue("".toUri())
            }

            applyNowBtn.setOnClickListener {
                val phoneNumber = phoneNumberEt.text.toString().replace("-", "")

                if (firstNameEt.text.toString().length >= 3) {
                    if (lastNameEt.text.toString().length >= 3) {
                        if (phoneNumber.length == 10) {
                            if (coverLetterDescEt.text.toString().length >= 3) {
                                if (fileName?.isNotEmpty() == true) {
                                    jobSeekerViewModel.applyJob(
                                        ApplyJobRequest(
                                            coverLetter = coverLetterDescEt.text.toString(),
                                            email = email ?: "",
                                            fullName = "${firstNameEt.text} ${lastNameEt.text}",
                                            jobId = args.jobId,
                                            phoneNo = phoneNumber,
                                            resumeName = fileName ?: "",
                                        )
                                    )
                                } else {
                                    showAlert("Please upload Resume")
                                }
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

        jobSeekerViewModel.applyJobData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.GONE
                    if (jobSeekerViewModel.resumeFileUri.toString().isNotEmpty()) {
                        //callUploadResumeApi(response.data?.id)
                    }
                    val action =
                        JobApplyFragmentDirections.actionJobApplyFragmentToJobProfileUploadSuccessFragment(
                            "ApplyJobScreen"
                        )
                    findNavController().navigate(action)
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        jobSeekerViewModel.uploadResumeData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    /*val action =
                        JobApplyFragmentDirections.actionJobApplyFragmentToJobProfileUploadSuccessFragment(
                            "ApplyJobScreen"
                        )
                    findNavController().navigate(action)*/
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        return binding?.root
    }

    private fun callUploadResumeApi(id: Int?) {

        val file = createTmpFileFromUri(jobSeekerViewModel.resumeFileUri)

        val requestFile: RequestBody? =
            file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val requestImage =
            requestFile?.let { MultipartBody.Part.createFormData("file", file?.name, it) }

        requestImage?.let { jobSeekerViewModel.uploadResume(id ?: 0, true, it) }
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

    private fun visibleResumeFile(enableGrayColor: Boolean = false) {
        binding?.uploadResumeLl?.gravity = Gravity.START
        binding?.uploadResumeTv?.visibility = View.GONE
        binding?.sizeLimitTv?.visibility = View.GONE
        binding?.uploadedResumeShapeLl?.visibility = View.VISIBLE
        binding?.resumeNameTv?.text = fileName

        if (enableGrayColor) {
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
        }
    }

    fun Uri.getName(context: Context): String? {
        val returnCursor = context.contentResolver.query(this, null, null, null, null)
        val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor?.moveToFirst()
        val fileName = nameIndex?.let { returnCursor.getString(it) }
        returnCursor?.close()
        return fileName
    }

    private fun checkPermission(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        jobSeekerViewModel.setResumeFileUriValue("".toUri())
    }
}