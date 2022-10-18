package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.WebViewActivity
import com.aaonri.app.data.immigration.model.Category
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationCenterDetailsBinding


class ImmigrationCenterDetails : Fragment() {
    var binding: FragmentImmigrationCenterDetailsBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var subTitle = mutableListOf<Category>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImmigrationCenterDetailsBinding.inflate(inflater, container, false)

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", "http://nces.ed.gov/pubsearch/pubsinfo.asp?pubid=2002165")
                activity?.startActivity(intent)
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }

        binding?.apply {
            selectAllImmigrationSpinner.setOnClickListener {
                immigrationViewModel.setImmigrationcenterCategoryIsClicked(true)
            }
            detailsTv.textSize = 16F

            immigrationViewModel.selectedImmigrationCenterItem.observe(viewLifecycleOwner) { it ->
                titleTv.text = it.title
                var category: List<Category> = it.categories
                immigrationViewModel.setImmigrationCenterDesc(it.categories[0])
                category.forEach {
                    if (!subTitle.contains(it)) {
                        subTitle.add(it)
                    }
                }
                immigrationViewModel.setImmigrationList(subTitle)
            }

            immigrationViewModel.immigrationCenterDesc.observe(viewLifecycleOwner) {
                if (it.id == 2) {
                    val ss = SpannableString(Html.fromHtml(it.description))
                    ss.setSpan(
                        clickableSpan1,
                        ss.indexOf("htt"),
                        ss.indexOf("2165"),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    subtitleTv.text = Html.fromHtml(it.title)
                    selectAllImmigrationSpinner.text = Html.fromHtml(it.title)
                    detailsTv.text = ss
                    detailsTv.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    detailsTv.fromHtml(it.description)
                    subtitleTv.text = Html.fromHtml(it.title)
                    selectAllImmigrationSpinner.text = Html.fromHtml(it.title)
                }
            }

            immigrationViewModel.immigrationCenterCategoryIsClicked.observe(viewLifecycleOwner) {
                if (it) {
                    val action =
                        ImmigrationCenterDetailsDirections.actionImmigrationCenterDetailsToImmigrationInfoCenterBottomSheet()
                    findNavController().navigate(action)
                    immigrationViewModel.setImmigrationcenterCategoryIsClicked(false)

                }
            }
            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}