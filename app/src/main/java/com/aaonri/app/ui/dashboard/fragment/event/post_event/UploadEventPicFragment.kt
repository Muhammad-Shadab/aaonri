package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Outline
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentCreateNewPasswordBinding
import com.aaonri.app.databinding.FragmentUploadEventPicBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class UploadEventPicFragment : Fragment() {
    var uploadEventPicBinding: FragmentUploadEventPicBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    val showingImagesList = mutableListOf<Uri>()
    var image1Uri = ""
    var image2Uri = ""
    var image3Uri = ""
    var image4Uri = ""
    var image1 = true
    var image2 = true
    var image3 = true
    var image4 = true
    var selectPicIndex = -1
    var rotatedBitmap: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uploadEventPicBinding = FragmentUploadEventPicBinding.inflate(inflater, container, false)
        val curveRadius = 10F
        setImageOnNavigatingBack()

//        postEventViewModel.addNavigationForStepper(ClassifiedConstant.UPLOAD_PIC_SCREEN)
        uploadEventPicBinding?.apply {
            uploadPicBtn.setOnClickListener {

                if (image1Uri.isEmpty() || image2Uri.isEmpty() || image3Uri.isEmpty() || image4Uri.isEmpty()) {
                    ImagePicker.with(requireActivity())
                        .compress(800)
                        .maxResultSize(1080, 1080)
                        .crop()
                        .createIntent { intent ->
                            startForClassifiedImageResult.launch(intent)
                            progressBarPicUpload.visibility = View.VISIBLE

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                selectedImage.outlineProvider = object : ViewOutlineProvider() {

                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(
                            0,
                            0,
                            view!!.width,
                            (view.height + curveRadius).toInt(),
                            curveRadius
                        )
                    }
                }
                selectedImage.clipToOutline = true
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

            eventUploadPicNextBtn.setOnClickListener {
                if (image1Uri.isNotEmpty()) {
                    if (!showingImagesList.contains(image1Uri.toUri())) {
                        showingImagesList.add(image1Uri.toUri())
                    }
                }
                if (image2Uri.isNotEmpty()) {
                    if (!showingImagesList.contains(image2Uri.toUri())) {
                        showingImagesList.add(image2Uri.toUri())
                    }
                }
                if (image3Uri.isNotEmpty()) {
                    if (!showingImagesList.contains(image3Uri.toUri())) {
                        showingImagesList.add(image3Uri.toUri())
                    }
                }
                if (image4Uri.isNotEmpty()) {
                    if (!showingImagesList.contains(image4Uri.toUri())) {
                        showingImagesList.add(image4Uri.toUri())
                    }
                }
                postEventViewModel.setListOfUploadImagesUri(showingImagesList)
                findNavController().navigate(R.id.action_uploadClassifiedPicFragment_to_addressDetailsClassifiedFragment)
            }
        }

        return uploadEventPicBinding?.root
    }


    private val startForClassifiedImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!
                if (image1Uri.isEmpty()) {
                    image1Uri = fileUri.toString()
                    setImage()
                } else if (image2Uri.isEmpty()) {
                    image2Uri = fileUri.toString()
                    setImage()
                } else if (image3Uri.isEmpty()) {
                    image3Uri = fileUri.toString()
                    setImage()
                } else if (image4Uri.isEmpty()) {
                    image4Uri = fileUri.toString()
                    setImage()
                }

                uploadEventPicBinding?.progressBarPicUpload?.visibility = View.GONE

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                uploadEventPicBinding?.progressBarPicUpload?.visibility = View.GONE
                //Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                uploadEventPicBinding?.progressBarPicUpload?.visibility = View.GONE
            }
        }

    private fun setImage() {
        if (image1 && image1Uri.isNotEmpty()) {
            selectPicIndex = 0
            uploadEventPicBinding?.uploadedImage1?.setImageURI(image1Uri.toUri())
            uploadEventPicBinding?.deleteImage1?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image1Uri.toUri())
            if (!showingImagesList.contains(image1Uri.toUri())) {
                showingImagesList.add(image1Uri.toUri())
            }
            image1 = false
            changeCardViewBg(0)
        } else if (image2 && image2Uri.isNotEmpty()) {
            selectPicIndex = 1
            uploadEventPicBinding?.uploadedImage2?.setImageURI(image2Uri.toUri())
            uploadEventPicBinding?.deleteImage2?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image2Uri.toUri())
            if (!showingImagesList.contains(image2Uri.toUri())) {
                showingImagesList.add(image2Uri.toUri())
            }
            image2 = false
            changeCardViewBg(1)
        } else if (image3 && image3Uri.isNotEmpty()) {
            selectPicIndex = 2
            uploadEventPicBinding?.uploadedImage3?.setImageURI(image3Uri.toUri())
            uploadEventPicBinding?.deleteImage3?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image3Uri.toUri())
            if (!showingImagesList.contains(image3Uri.toUri())) {
                showingImagesList.add(image3Uri.toUri())
            }
            image3 = false
            changeCardViewBg(2)
        } else if (image4 && image4Uri.isNotEmpty()) {
            selectPicIndex = 3
            uploadEventPicBinding?.uploadedImage4?.setImageURI(image4Uri.toUri())
            uploadEventPicBinding?.deleteImage4?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image4Uri.toUri())
            if (!showingImagesList.contains(image4Uri.toUri())) {
                showingImagesList.add(image4Uri.toUri())
            }
            image4 = false
            changeCardViewBg(3)
        }
        disableUploadBtnColor()
    }

    private fun disableUploadBtnColor() {
        if (image1Uri.isNotEmpty() && image2Uri.isNotEmpty() && image3Uri.isNotEmpty() && image4Uri.isNotEmpty()) {
            uploadEventPicBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_disabled_add_btn
                )
            })
        } else {
            uploadEventPicBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_add_image_icon
                )
            })
        }
    }

    private fun setImageOnNavigatingBack() {
        if (image1Uri.isNotEmpty()) {
            selectPicIndex = 0
            uploadEventPicBinding?.uploadedImage1?.setImageURI(image1Uri.toUri())
            uploadEventPicBinding?.deleteImage1?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image1Uri.toUri())
            if (!showingImagesList.contains(image1Uri.toUri())) {
                showingImagesList.add(image1Uri.toUri())
            }
            image1 = false
            changeCardViewBg(0)
        }
        if (image2Uri.isNotEmpty()) {
            selectPicIndex = 1
            uploadEventPicBinding?.uploadedImage2?.setImageURI(image2Uri.toUri())
            uploadEventPicBinding?.deleteImage2?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image2Uri.toUri())
            if (!showingImagesList.contains(image2Uri.toUri())) {
                showingImagesList.add(image2Uri.toUri())
            }
            image2 = false
            changeCardViewBg(1)
        }
        if (image3Uri.isNotEmpty()) {
            selectPicIndex = 2
            uploadEventPicBinding?.uploadedImage3?.setImageURI(image3Uri.toUri())
            uploadEventPicBinding?.deleteImage3?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image3Uri.toUri())
            if (!showingImagesList.contains(image3Uri.toUri())) {
                showingImagesList.add(image3Uri.toUri())
            }
            image3 = false
            changeCardViewBg(2)
        }
        if (image4Uri.isNotEmpty()) {
            selectPicIndex = 3
            uploadEventPicBinding?.uploadedImage4?.setImageURI(image4Uri.toUri())
            uploadEventPicBinding?.deleteImage4?.visibility = View.VISIBLE
            uploadEventPicBinding?.selectedImage?.setImageURI(image4Uri.toUri())
            if (!showingImagesList.contains(image4Uri.toUri())) {
                showingImagesList.add(image4Uri.toUri())
            }
            image4 = false
            changeCardViewBg(3)
        }
        disableUploadBtnColor()
    }

    private fun deleteImage(index: Int) {
        if (index == 0) {
            selectPicIndex = 0
            uploadEventPicBinding?.uploadedImage1?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            uploadEventPicBinding?.deleteImage1?.visibility = View.GONE
            changeCardViewBg(0)
            if (showingImagesList.contains(image1Uri.toUri())) {
                showingImagesList.remove(image1Uri.toUri())
            }
            image1Uri = ""
            image1 = true
        } else if (index == 1) {
            selectPicIndex = 1
            uploadEventPicBinding?.uploadedImage2?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            if (showingImagesList.contains(image2Uri.toUri())) {
                showingImagesList.remove(image2Uri.toUri())
            }
            uploadEventPicBinding?.deleteImage2?.visibility = View.GONE
            changeCardViewBg(1)
            image2Uri = ""
            image2 = true
        } else if (index == 2) {
            selectPicIndex = 2
            uploadEventPicBinding?.uploadedImage3?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            if (showingImagesList.contains(image3Uri.toUri())) {
                showingImagesList.remove(image3Uri.toUri())
            }
            uploadEventPicBinding?.deleteImage3?.visibility = View.GONE
            changeCardViewBg(2)
            image3Uri = ""
            image3 = true
        } else if (index == 3) {
            selectPicIndex = 3
            uploadEventPicBinding?.uploadedImage4?.setImageDrawable(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_uplaoded_image
                )
            })
            if (showingImagesList.contains(image4Uri.toUri())) {
                showingImagesList.remove(image4Uri.toUri())
            }
            uploadEventPicBinding?.deleteImage4?.visibility = View.GONE
            changeCardViewBg(3)
            image4Uri = ""
            image4 = true
        }

        if (image1Uri.isEmpty() && image2Uri.isEmpty() && image3Uri.isEmpty() && image4Uri.isEmpty()) {
            uploadEventPicBinding?.selectedImage?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_imageview_placeholder
                )
            })
        }

        if (image1Uri.isNotEmpty() && image2Uri.isNotEmpty() && image3Uri.isNotEmpty() && image4Uri.isNotEmpty()) {
            uploadEventPicBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_disabled_add_btn
                )
            })
        } else {
            uploadEventPicBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_add_image_icon
                )
            })
        }
        setImageAfterDelete()
    }

    private fun setImageAfterDelete() {
        if (showingImagesList.size != 0) {
            if (showingImagesList[showingImagesList.size - 1] == image4Uri.toUri()) {
                uploadEventPicBinding?.selectedImage?.setImageURI(showingImagesList[showingImagesList.size - 1])
                changeCardViewBg(3)
            } else if (showingImagesList[showingImagesList.size - 1] == image3Uri.toUri()) {
                uploadEventPicBinding?.selectedImage?.setImageURI(showingImagesList[showingImagesList.size - 1])
                changeCardViewBg(2)
            } else if (showingImagesList[showingImagesList.size - 1] == image2Uri.toUri()) {
                uploadEventPicBinding?.selectedImage?.setImageURI(showingImagesList[showingImagesList.size - 1])
                changeCardViewBg(1)
            } else if (showingImagesList[showingImagesList.size - 1] == image1Uri.toUri()) {
                uploadEventPicBinding?.selectedImage?.setImageURI(showingImagesList[showingImagesList.size - 1])
                changeCardViewBg(0)
            }
        }else{
            changeCardViewBg(4)
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
                uploadEventPicBinding?.uploadedImageFl1?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl2?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl3?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl4?.setStrokeColor(
                    it2
                )
            }
        }
        else if (selectedImageIndex == 1 && image2Uri.isNotEmpty()) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl1?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl2?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl3?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl4?.setStrokeColor(
                    it2
                )
            }
        }
        else if (selectedImageIndex == 2 && image3Uri.isNotEmpty()) {

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl1?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl2?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl3?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl4?.setStrokeColor(
                    it2
                )
            }

        }
        else if (selectedImageIndex == 3 && image4Uri.isNotEmpty()) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl1?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl2?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl3?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.blueBtnColor
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl4?.setStrokeColor(
                    it2
                )
            }
        }
        else {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl1?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl2?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl3?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadEventPicBinding?.uploadedImageFl4?.setStrokeColor(
                    it2
                )
            }
        }
    }
}