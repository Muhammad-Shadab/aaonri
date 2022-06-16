package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedPostSuccessBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedPostSuccessBottom : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    var bottomBinding: FragmentClassifiedPostSuccessBottomBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        bottomBinding =
            FragmentClassifiedPostSuccessBottomBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addStepViewLastTick(true)

        Toast.makeText(
            context,
            "city name = " + postClassifiedViewModel.classifiedAddressDetailsMap[ClassifiedConstant.CITY_NAME],
            Toast.LENGTH_SHORT
        ).show()


        /* Toast.makeText(
             context,
             "" + postClassifiedViewModel.isProductNewCheckBox,
             Toast.LENGTH_SHORT
         ).show()
 */

        bottomBinding?.apply {
            viewYourClassifiedBtn.setOnClickListener {
                activity?.finish()
            }
        }

        return bottomBinding?.root
    }
}