package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aaonri.app.R
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertisePostSuccessBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvertisePostSuccessFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentAdvertisePostSuccessBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding =
            FragmentAdvertisePostSuccessBinding.inflate(inflater, container, false)

        binding?.apply {

            postAdvertiseViewModel.setStepViewLastTick(true)

            if (postAdvertiseViewModel.isUpdateAdvertise){
                textView6.text = "Your advertisement will be posted on aaonri\nweb & mobile platforms within 24 hrs after the \n completion of the approval process!"
            }

            viewYourAdvertiseBtn.setOnClickListener {
                val intent = Intent()
                intent.putExtra("callAdvertiseApi", true)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }

        }
        return binding?.root
    }


}