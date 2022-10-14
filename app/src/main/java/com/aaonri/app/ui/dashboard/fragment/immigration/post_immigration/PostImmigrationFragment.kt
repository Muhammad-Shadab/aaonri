package com.aaonri.app.ui.dashboard.fragment.immigration.post_immigration

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.WebViewActivity
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.PostDiscussionRequest
import com.aaonri.app.data.immigration.model.UpdateDiscussionRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentPostImmigrationBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar

class PostImmigrationFragment : Fragment() {
    var binding: FragmentPostImmigrationBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var discussionCategoryResponseItem: DiscussionCategoryResponseItem? = null
    val args: PostImmigrationFragmentArgs by navArgs()
    var discussionData: Discussion? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostImmigrationBinding.inflate(layoutInflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val ss =
            SpannableString(resources.getString(R.string.bu_submitting_you_agree_to_our_privacy_policy))

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", "https://aaonri.com/privacy-policy")
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

        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", "https://aaonri.com/terms-&-conditions")
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

        Toast.makeText(context, "${ss.indexOf("Privacy")}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "${ss.indexOf("&")}", Toast.LENGTH_SHORT).show()
        ss.setSpan(clickableSpan1, 31, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(clickableSpan2, 49, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding?.apply {

            privacyPolicyTv.text = ss
            privacyPolicyTv.movementMethod = LinkMovementMethod.getInstance()

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            selectImmigrationCategory.setOnClickListener {
                val action =
                    PostImmigrationFragmentDirections.actionPostImmigrationFragmentToImmigrationCategoryBottomSheet(
                        "PostImmigrationScreen"
                    )
                findNavController().navigate(action)
            }

            submitBtn.setOnClickListener {
                if (discussionTopicEt.text.toString().length >= 3) {
                    if (selectImmigrationCategory.text.toString().isNotEmpty()) {
                        if (descEt.text.toString().length >= 3) {
                            if (agreeCheckbox.isChecked) {
                                if (args.isUpdateImmigration) {
                                    /*immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(
                                        false
                                    )
                                    immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(
                                        false
                                    )*/
                                    immigrationViewModel.updateDiscussion(
                                        UpdateDiscussionRequest(
                                            discCatId = if (discussionCategoryResponseItem?.discCatId != null) discussionCategoryResponseItem!!.discCatId else if (discussionData?.discCatId != null) discussionData!!.discCatId else 0,
                                            discussionDesc = descEt.text.toString(),
                                            discussionTopic = discussionTopicEt.text.toString(),
                                            userId = email ?: "",
                                            discussionId = if (discussionData?.discussionId != null) discussionData?.discussionId!! else 0
                                        )
                                    )
                                } else {
                                    /*immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(
                                        false
                                    )
                                    immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(
                                        false
                                    )*/
                                    immigrationViewModel.postDiscussion(
                                        PostDiscussionRequest(
                                            discCatId = if (discussionCategoryResponseItem?.discCatId != null) discussionCategoryResponseItem!!.discCatId else 0,
                                            discussionDesc = descEt.text.toString(),
                                            discussionTopic = discussionTopicEt.text.toString(),
                                            userId = email ?: ""
                                        )
                                    )
                                }
                            } else {
                                showAlert("Please agree to Terms & Conditions")
                            }
                        } else {
                            showAlert("Please enter valid description")
                        }
                    } else {
                        showAlert("Please choose category")
                    }
                } else {
                    showAlert("Please enter valid discussion topic")
                }
            }

            if (args.isUpdateImmigration) {
                registrationText.text = "Update Discussion"
                immigrationViewModel.selectedDiscussionItem.observe(viewLifecycleOwner) { discussion ->
                    discussionData = discussion
                    discussionTopicEt.setText(discussion.discussionTopic)
                    selectImmigrationCategory.text = discussion.discCatValue
                    descEt.setText(discussion.discussionDesc)
                }
            }

            descEt.addTextChangedListener { editable ->
                descLength.text = "${editable.toString().length}/257"
            }

        }

        immigrationViewModel.selectedPostingDiscussionScreenCategory.observe(viewLifecycleOwner) {
            if (it != null) {
                discussionCategoryResponseItem = it
                binding?.selectImmigrationCategory?.text = it.discCatValue
            }
        }

        immigrationViewModel.postDiscussionData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        if (response.data?.discussionId != null) {
                            // success
                            findNavController().navigateUp()
                        } else {
                            showAlert("Topic already available")
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
                immigrationViewModel.postDiscussionData.postValue(null)
            }
        }

        immigrationViewModel.updateDiscussionData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        if (response.data?.discussionId != null) {
                            // success
                            findNavController().navigateUp()
                        } else {
                            showAlert("Something went wrong")
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
                immigrationViewModel.updateDiscussionData.postValue(null)
            }
        }

        return binding?.root
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
        immigrationViewModel.selectedPostingDiscussionScreenCategory.postValue(null)
        binding = null
    }
}