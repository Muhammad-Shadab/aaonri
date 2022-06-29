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
    var selectPicIndex = -1
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
                    ImagePicker.with(requireActivity())
                        .compress(800)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForClassifiedImageResult.launch(intent)
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
                if (image1Uri.isNotBlank()) {
                    selectedImage.load(image1Uri)
                    changeCardViewBg(0)
                }
            }
            uploadedImage2.setOnClickListener {
                if (image2Uri.isNotBlank()) {
                    selectedImage.load(image2Uri)
                    changeCardViewBg(1)
                }
            }
            uploadedImage3.setOnClickListener {
                if (image3Uri.isNotBlank()) {
                    selectedImage.load(image3Uri)
                    changeCardViewBg(2)
                }
            }
            uploadedImage4.setOnClickListener {
                if (image4Uri.isNotBlank()) {
                    selectedImage.load(image4Uri)
                    changeCardViewBg(3)
                }
            }

            classifiedUploadPicNextBtn.setOnClickListener {

                if (image1Uri.isNotEmpty()) {
                    if (!listOfImagesUri.contains(image1Uri.toUri())) {
                        listOfImagesUri.add(image1Uri.toUri())
                    }
                }
                if (image2Uri.isNotEmpty()) {
                    if (!listOfImagesUri.contains(image2Uri.toUri())) {
                        listOfImagesUri.add(image2Uri.toUri())
                    }
                }
                if (image3Uri.isNotEmpty()) {
                    if (!listOfImagesUri.contains(image3Uri.toUri())) {
                        listOfImagesUri.add(image3Uri.toUri())
                    }
                }
                if (image4Uri.isNotEmpty()) {
                    if (!listOfImagesUri.contains(image4Uri.toUri())) {
                        listOfImagesUri.add(image4Uri.toUri())
                    }
                }

                postClassifiedViewModel.setListOfUploadImagesUri(listOfImagesUri)
                findNavController().navigate(R.id.action_uploadClassifiedPicFragment_to_addressDetailsClassifiedFragment)
            }


        }

        return uploadClassifiedBinding?.root
    }

    private val startForClassifiedImageResult =
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

                setImage()

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {

            }
        }

    private fun setImage() {
        if (image1 && image1Uri.isNotEmpty()) {
            selectPicIndex = 0
            uploadClassifiedBinding?.uploadedImage1?.setImageURI(image1Uri.toUri())
            uploadClassifiedBinding?.deleteImage1?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.setImageURI(image1Uri.toUri())
            image1 = false
            changeCardViewBg(0)
        } else if (image2 && image2Uri.isNotEmpty()) {
            selectPicIndex = 1
            uploadClassifiedBinding?.uploadedImage2?.setImageURI(image2Uri.toUri())
            uploadClassifiedBinding?.deleteImage2?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.setImageURI(image2Uri.toUri())
            image2 = false
            changeCardViewBg(1)
        } else if (image3 && image3Uri.isNotEmpty()) {
            selectPicIndex = 2
            uploadClassifiedBinding?.uploadedImage3?.setImageURI(image3Uri.toUri())
            uploadClassifiedBinding?.deleteImage3?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.setImageURI(image3Uri.toUri())
            image3 = false
            changeCardViewBg(2)
        } else if (image4 && image4Uri.isNotEmpty()) {
            selectPicIndex = 3
            uploadClassifiedBinding?.uploadedImage4?.setImageURI(image4Uri.toUri())
            uploadClassifiedBinding?.deleteImage4?.visibility = View.VISIBLE
            uploadClassifiedBinding?.selectedImage?.setImageURI(image4Uri.toUri())
            image4 = false
            changeCardViewBg(3)
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

    private fun deleteImage(index: Int) {
        if (index == 0) {
            selectPicIndex = 0
            uploadClassifiedBinding?.uploadedImage1?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            uploadClassifiedBinding?.deleteImage1?.visibility = View.GONE
            removeBorder(0)
            if (listOfImagesUri.contains(image1Uri.toUri())) {
                listOfImagesUri.remove(image1Uri.toUri())
            }
            image1Uri = ""
            image1 = true
        } else if (index == 1) {
            selectPicIndex = 1
            uploadClassifiedBinding?.uploadedImage2?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            if (listOfImagesUri.contains(image2Uri.toUri())) {
                listOfImagesUri.remove(image2Uri.toUri())
            }
            uploadClassifiedBinding?.deleteImage2?.visibility = View.GONE
            removeBorder(1)
            image2Uri = ""
            image2 = true
        } else if (index == 2) {
            selectPicIndex = 2
            uploadClassifiedBinding?.uploadedImage3?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            if (listOfImagesUri.contains(image3Uri.toUri())) {
                listOfImagesUri.remove(image3Uri.toUri())
            }
            uploadClassifiedBinding?.deleteImage3?.visibility = View.GONE
            removeBorder(2)
            image3Uri = ""
            image3 = true
        } else if (index == 3) {
            selectPicIndex = 3
            uploadClassifiedBinding?.uploadedImage4?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            if (listOfImagesUri.contains(image4Uri.toUri())) {
                listOfImagesUri.remove(image4Uri.toUri())
            }
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

    /*private fun setImageAfterDelete() {
        listOfImagesUri.forEach {
            if (image4Uri.isEmpty()){
                uploadClassifiedBinding?.selectedImage?.setImageURI(image4Uri.toUri())
            }else if (image3Uri.isEmpty()){
                uploadClassifiedBinding?.selectedImage?.setImageURI(image4Uri.toUri())
            }
        }
    }*/

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
                    R.color.blueBtnColor
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
                    R.color.blueBtnColor
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
                    R.color.blueBtnColor
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
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        }
    }
}