package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentUploadClassifiedPicBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.net.URI

@AndroidEntryPoint
class UploadClassifiedPicFragment : Fragment() {
    var uploadClassifiedBinding: FragmentUploadClassifiedPicBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val listOfImagesUri = mutableListOf<Uri>()
    var image1Uri = ""
    var image2Uri = ""
    var image3Uri = ""
    var image4Uri = ""
    var image1 = true
    var image2 = true
    var image3 = true
    var image4 = true
    var selectedBiggerImage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadClassifiedBinding =
            FragmentUploadClassifiedPicBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.UPLOAD_PIC_SCREEN)

        uploadClassifiedBinding?.apply {

            uploadPicBtn.setOnClickListener {
                if (image1Uri.isEmpty() || image2Uri.isEmpty() || image3Uri.isEmpty() || image4Uri.isEmpty()) {
                    ImagePicker.with(activity!!)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                } else {

                }
            }

            deleteImage1.setOnClickListener {
                deleteImage(0)
            }
            deleteImage2.setOnClickListener {
                deleteImage(1)
            }
            deleteImage3.setOnClickListener {
                deleteImage(2)
            }
            deleteImage4.setOnClickListener {
                deleteImage(3)
            }

            uploadedImage1.setOnClickListener {
                if (image1Uri.isNotBlank()){
                    selectedImage.load(image1Uri)
                    changeCardViewBg(0)
                }
            }
            uploadedImage2.setOnClickListener {
                if (image2Uri.isNotBlank()){
                    selectedImage.load(image2Uri)
                    changeCardViewBg(1)
                }
            }
            uploadedImage3.setOnClickListener {
                if (image3Uri.isNotBlank()){
                    selectedImage.load(image3Uri)
                    changeCardViewBg(2)
                }
            }
            uploadedImage4.setOnClickListener {
                if (image4Uri.isNotBlank()){
                    selectedImage.load(image4Uri)
                    changeCardViewBg(3)
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
                val fileUri = data?.data!!

                if (image1Uri.isEmpty()) {
                    image1Uri = fileUri.toString()
                } else if (image2Uri.isEmpty()) {
                    image2Uri = fileUri.toString()
                } else if (image3Uri.isEmpty()) {
                    image3Uri = fileUri.toString()
                } else if (image4Uri.isEmpty()) {
                    image4Uri = fileUri.toString()
                }

                listOfImagesUri.add(fileUri)
                setImage()


            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {

            }
        }

    private fun setImage() {
        if (image1 && image1Uri.isNotEmpty()) {
            selectedBiggerImage = image1Uri
            uploadClassifiedBinding?.uploadedImage1?.load(image1Uri)
            uploadClassifiedBinding?.deleteImage1?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.load(selectedBiggerImage)
            image1 = false
            changeCardViewBg(0)
        } else if (image2 && image2Uri.isNotEmpty()) {
            selectedBiggerImage = image2Uri
            uploadClassifiedBinding?.uploadedImage2?.load(image2Uri)
            uploadClassifiedBinding?.deleteImage2?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.load(selectedBiggerImage)
            image2 = false
            changeCardViewBg(1)
        } else if (image3 && image3Uri.isNotEmpty()) {
            selectedBiggerImage = image3Uri
            uploadClassifiedBinding?.uploadedImage3?.load(image3Uri)
            uploadClassifiedBinding?.deleteImage3?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.load(selectedBiggerImage)
            image3 = false
            changeCardViewBg(2)
        } else if (image4 && image4Uri.isNotEmpty()) {
            selectedBiggerImage = image4Uri
            uploadClassifiedBinding?.uploadedImage4?.load(image4Uri)
            uploadClassifiedBinding?.deleteImage4?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.load(selectedBiggerImage)
            image4 = false
            changeCardViewBg(3)
        }

    }

    private fun deleteImage(index: Int) {
        if (index == 0) {
            uploadClassifiedBinding?.uploadedImage1?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage1?.visibility = View.GONE
            removeBorder(0)
            image1Uri = ""
            image1 = true
        } else if (index == 1) {
            uploadClassifiedBinding?.uploadedImage2?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage2?.visibility = View.GONE
            removeBorder(1)
            image2Uri = ""
            image2 = true
        } else if (index == 2) {
            uploadClassifiedBinding?.uploadedImage3?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage3?.visibility = View.GONE
            removeBorder(2)
            image3Uri = ""
            image3 = true
        } else if (index == 3) {
            uploadClassifiedBinding?.uploadedImage4?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage4?.visibility = View.GONE
            removeBorder(3)
            image4Uri = ""
            image4 = true
        }

        if (image1Uri.isEmpty() && image2Uri.isEmpty() && image3Uri.isEmpty() && image4Uri.isEmpty()) {
            uploadClassifiedBinding?.selectedImage?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_image_placeholder
                )
            })
        }

        if (image1Uri.isNotEmpty() && image2Uri.isNotEmpty() && image3Uri.isNotEmpty() && image4Uri.isNotEmpty()) {
            uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_disabled_add_btn
                )
            })
        } else {
            uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_add_image_icon
                )
            })
        }
    }

    private fun removeBorder(index: Int) {
        when (index) {
            0 -> {
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }?.let { it2 ->
                    uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                        it2
                    )
                }
            }
            1 -> {
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }?.let { it2 ->
                    uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                        it2
                    )
                }
            }
            2 -> {
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }?.let { it2 ->
                    uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                        it2
                    )
                }
            }
            3 -> {
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }?.let { it2 ->
                    uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                        it2
                    )
                }
            }
        }
    }

    private fun changeCardViewBg(selectedImageIndex: Int) {

        if (selectedImageIndex == 0 && image1Uri.isNotEmpty()) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 1) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 2) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 3) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        }
    }
}