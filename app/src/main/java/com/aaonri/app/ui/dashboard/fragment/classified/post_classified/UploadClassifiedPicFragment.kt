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
                Toast.makeText(context, "${listOfImagesUri.size}", Toast.LENGTH_SHORT).show()
                ImagePicker.with(activity!!)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
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

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun setImage(listOfImagesUri: MutableList<Uri>) {
        // mProfileUri = fileUri
        try {
            if (image1) {
                uploadClassifiedBinding?.uploadedImage1?.setImageURI(listOfImagesUri[0])
                image1 = false
            } else if (image2) {
                uploadClassifiedBinding?.uploadedImage2?.setImageURI(listOfImagesUri[1])
                image2 = false
            } else if (image3) {
                uploadClassifiedBinding?.uploadedImage3?.setImageURI(listOfImagesUri[2])
                image3 = false
            } else if (image4) {
                uploadClassifiedBinding?.uploadedImage4?.setImageURI(listOfImagesUri[3])
                image4 = false
            }
        } catch (e: Exception) {

        }
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK) {
             //Image Uri will not be null for RESULT_OK
             val uri: Uri = data?.data!!

             // Use Uri object instead of File to avoid storage permissions
             uploadClassifiedBinding?.uploadedImage1?.load(uri)
         } else if (resultCode == ImagePicker.RESULT_ERROR) {
             Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
         } else {
             Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
         }
     }*/

}