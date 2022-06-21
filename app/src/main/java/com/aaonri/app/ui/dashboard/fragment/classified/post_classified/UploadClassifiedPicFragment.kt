package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentUploadClassifiedPicBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class UploadClassifiedPicFragment : Fragment() {
    var uploadClassifiedBinding: FragmentUploadClassifiedPicBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val listOfImagesUri = mutableListOf<Uri>()
    var image1 = true
    var image2 = true
    var image3 = true
    var image4 = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadClassifiedBinding =
            FragmentUploadClassifiedPicBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.UPLOAD_PIC_SCREEN)

        uploadClassifiedBinding?.apply {

            uploadPicBtn.setOnClickListener {
                if (listOfImagesUri.size < 4) {
                    ImagePicker.with(activity!!)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                } else {

                }
            }

                uploadedImage1.setOnClickListener {
                    if (listOfImagesUri[0].toString().isNotEmpty()){
                        selectedImage.load(listOfImagesUri[0])
                    }
                }
                uploadedImage2.setOnClickListener {
                    if (listOfImagesUri[1].toString().isNotEmpty()){
                        selectedImage.load(listOfImagesUri[1])
                    }
                }
                uploadedImage3.setOnClickListener {
                    if (listOfImagesUri[2].toString().isNotEmpty()){
                        selectedImage.load(listOfImagesUri[2])
                    }
                }
                uploadedImage4.setOnClickListener {
                    if (listOfImagesUri[3].toString().isNotEmpty()){
                        selectedImage.load(listOfImagesUri[3])
                    }
                }


            classifiedUploadPicNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_uploadClassifiedPicFragment_to_addressDetailsClassifiedFragment)
            }
        }

        return uploadClassifiedBinding?.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                uploadClassifiedBinding?.selectedImage?.setImageURI(fileUri)

                listOfImagesUri.add(fileUri)

                setImage(listOfImagesUri)

                if (listOfImagesUri.size < 4) {
                    uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.ic_add_image_icon
                        )
                    })
                } else {
                    uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.ic_disabled_add_btn
                        )
                    })
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {

            }
        }

    private fun setImage(listOfImagesUri: MutableList<Uri>) {
        if (image1 && listOfImagesUri[0].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage1?.setImageURI(listOfImagesUri[0])
            image1 = false
        } else if (image2 && listOfImagesUri[1].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage2?.setImageURI(listOfImagesUri[1])
            image2 = false
        } else if (image3 && listOfImagesUri[2].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage3?.setImageURI(listOfImagesUri[2])
            image3 = false
        } else if (image4 && listOfImagesUri[3].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage4?.setImageURI(listOfImagesUri[3])
            image4 = false
        }
    }
}