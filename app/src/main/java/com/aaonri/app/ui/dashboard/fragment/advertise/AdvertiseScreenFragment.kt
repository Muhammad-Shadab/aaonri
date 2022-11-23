package com.aaonri.app.ui.dashboard.fragment.advertise

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentAdvertiseScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AdvertiseScreenFragment : Fragment() {
    var binding: FragmentAdvertiseScreenBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    var layoutManager: LinearLayoutManager? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var advertiseAdapter: AdvertiseAdapter? = null
    var isAdvertiseExpired: Boolean? = null
    var data: List<AllAdvertiseResponseItem>? = null

    //var advertiseIdList = mutableListOf<Int>()

    var advertisementList = mutableListOf<AllAdvertiseResponseItem>()

    var isGuestUser = false

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }


        layoutManager = LinearLayoutManager(context)

        val ss = SpannableString(resources.getString(R.string.login_to_view_Advertisement))
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity?.finish()
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
        ss.setSpan(clickableSpan1, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        /** Dialog for edit/update profile and logout user **/
        val updateLogoutDialog = Dialog(requireContext())
        updateLogoutDialog.setContentView(R.layout.update_profile_dialog)
        updateLogoutDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )

        updateLogoutDialog.setCancelable(false)
        val editProfileBtn =
            updateLogoutDialog.findViewById<TextView>(R.id.editProfileBtn)
        val logOutBtn =
            updateLogoutDialog.findViewById<TextView>(R.id.logOutBtn)
        val closeDialogBtn =
            updateLogoutDialog.findViewById<ImageView>(R.id.closeDialogBtn)
        val dialogProfileIv =
            updateLogoutDialog.findViewById<ImageView>(R.id.profilePicIv)
        val userNameTv =
            updateLogoutDialog.findViewById<TextView>(R.id.userNameTv)
        val userEmailTv =
            updateLogoutDialog.findViewById<TextView>(R.id.userEmailTv)

        userNameTv.text = userName
        userEmailTv.text = email
        context?.let {
            Glide.with(it).load(profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop().error(R.drawable.profile_pic_placeholder)
                .into(dialogProfileIv)
        }

        val window: Window? = updateLogoutDialog.window
        val wlp: WindowManager.LayoutParams? = window?.attributes

        wlp?.gravity = Gravity.TOP
        window?.attributes = wlp

        logOutBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure you want to Logout?")
            builder.setPositiveButton("OK") { dialog, which ->

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.BLOCKED_USER_ID, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_EMAIL, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_ZIP_CODE, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_CITY, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_STATE, "")

                context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                    ?.set(Constant.IS_USER_LOGIN, false)

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_PROFILE_PIC, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.GMAIL_FIRST_NAME, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.GMAIL_LAST_NAME, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_INTERESTED_SERVICES, "")

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_NAME, "")

                context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                    ?.set(Constant.IS_JOB_RECRUITER, false)

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_PHONE_NUMBER, "")

                context?.let { it1 -> PreferenceManager<Int>(it1) }
                    ?.set(Constant.USER_ID, 0)

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.gmail_client_id))
                    .requestEmail()
                    .build()

                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
                mGoogleSignInClient.signOut()

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->

            }
            builder.show()
        }

        editProfileBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
            val action =
                AdvertiseScreenFragmentDirections.actionAdvertiseScreenFragmentToUpdateProfileFragment(false)
            findNavController().navigate(action)
        }

        closeDialogBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
        }

        /** Dialog for guest user **/
        val guestUserLoginDialog = Dialog(requireContext())
        guestUserLoginDialog.setContentView(R.layout.guest_user_login_dialog)
        guestUserLoginDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )
        guestUserLoginDialog.setCancelable(false)
        val dismissBtn =
            guestUserLoginDialog.findViewById<TextView>(R.id.dismissDialogTv)
        val loginBtn =
            guestUserLoginDialog.findViewById<TextView>(R.id.loginDialogTv)
        loginBtn.setOnClickListener {
            activity?.finish()
        }
        dismissBtn.setOnClickListener {
            guestUserLoginDialog.dismiss()
        }

        advertiseAdapter = AdvertiseAdapter { selectedService, isMoreMenuBtnClicked ->

            var d1 = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                .format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .parse(selectedService.toDate.split("T")[0])
                )

            val d2 = getCalculatedDate("MM-dd-yyyy", 0)

            val sdf = SimpleDateFormat("MM-dd-yyyy")

            val firstDate: Date = sdf.parse(d1)
            val secondDate: Date = sdf.parse(d2)

            val cmp = firstDate.compareTo(secondDate)
            //val cmp1 = firstDate.compareTo(current)

            //do not remove this code or do not change this code*
            if (cmp > 0) {
                isAdvertiseExpired = false
            } else {
                /**Current date is greater then advertise date**/
                /** Advertise Expired **/
                isAdvertiseExpired = true
            }

            if (isMoreMenuBtnClicked) {
                val action =
                    AdvertiseScreenFragmentDirections.actionAdvertiseScreenFragmentToUpdateAndDeleteBottomFragment(
                        selectedService.advertisementId,
                        true,
                        isAdvertiseExpired!!
                    )
                findNavController().navigate(action)
            } else {
                val action =
                    AdvertiseScreenFragmentDirections.actionAdvertiseScreenFragmentToAdvertisementDetailsFragment(
                        selectedService.advertisementId
                    )
                findNavController().navigate(action)
            }
        }

        binding = FragmentAdvertiseScreenBinding.inflate(inflater, container, false)
        binding?.apply {

            loginToViewAdvertisement.textSize = 16F
            loginToViewAdvertisement.text = ss
            loginToViewAdvertisement.movementMethod = LinkMovementMethod.getInstance()

            loginToViewAdvertisement.setOnClickListener {
                activity?.finish()
            }

            searchViewIcon.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
            }

            profilePicCv.setOnClickListener {
                if (isGuestUser) {
                    activity?.finish()
                } else {
                    updateLogoutDialog.show()
                }
            }

            floatingActionBtnEvents.setOnClickListener {
                if (isUserLogin == true) {
                    val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                    startActivityForResult(intent, 3)
                } else {
                    guestUserLoginDialog.show()
                }

            }

            context?.let {
                Glide.with(it).load(profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }

            /** close keyboeard while scrolling**/
            recyclerViewAdvertise.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

            recyclerViewAdvertise.layoutManager = layoutManager
            recyclerViewAdvertise.adapter = advertiseAdapter
            /*if (isGuestUser) {
                isGuestUser = true
                binding?.loginToViewAdvertisement?.visibility = View.VISIBLE
                binding?.yourText?.visibility = View.GONE
                binding?.postingofAdTv?.visibility = View.GONE
                binding?.recyclerViewAdvertise?.visibility = View.GONE
            } else {
                isGuestUser = false
                binding?.loginToViewAdvertisement?.visibility = View.GONE
                binding?.yourText?.visibility = View.VISIBLE
                binding?.postingofAdTv?.visibility = View.VISIBLE
                binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                //classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.VISIBLE
            }*/

            /*nestedScrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                }
            })
*/
            if (advertiseViewModel.searchQueryToSetOnSearchView.isNotEmpty()) {
                binding?.searchView?.setText(advertiseViewModel.searchQueryToSetOnSearchView)
                advertiseViewModel.setSearchQueryFromHomeScreenValue(advertiseViewModel.searchQueryToSetOnSearchView)
                advertiseViewModel.setSearchQueryToSetOnSearchViewValue("")
            }

            if (searchView.text.toString().isEmpty()) {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
            } else {
                cancelbutton.visibility = View.VISIBLE
                searchViewIcon.visibility = View.GONE
            }

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    advertiseViewModel.setSearchQueryFromHomeScreenValue(keyword.toString())
                    if (keyword.toString().isEmpty()) {
                        cancelbutton.visibility = View.GONE
                        searchViewIcon.visibility = View.VISIBLE
                    } else {
                        cancelbutton.visibility = View.VISIBLE
                        searchViewIcon.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            cancelbutton.setOnClickListener {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
                if (searchView.text.isNotEmpty()) {
                    searchView.setText("")
                }

            }

            advertiseViewModel.advertiseContentScrollToTop.observe(viewLifecycleOwner) {
                if (it) {
                    layoutManager?.smoothScrollToPosition(recyclerViewAdvertise, null, 0)
                    advertiseViewModel.setAdvertiseContentScrollToTop(false)
                }
            }

        }
        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            isGuestUser = it
            if (it) {
                binding?.loginToViewAdvertisement?.visibility = View.VISIBLE
                binding?.yourText?.visibility = View.GONE
                //binding?.postingofAdTv?.visibility = View.GONE
                binding?.recyclerViewAdvertise?.visibility = View.GONE
            } else {
                binding?.loginToViewAdvertisement?.visibility = View.GONE
                binding?.yourText?.visibility = View.VISIBLE
                //binding?.postingofAdTv?.visibility = View.VISIBLE
                binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                //classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.VISIBLE
            }
        }

        advertiseViewModel.allAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    if (response.data?.isEmpty() == true && binding?.searchView?.text.toString().isEmpty()) {
                        binding?.searchView?.isEnabled = false
                        binding?.noResultFound?.visibility = View.VISIBLE
                        binding?.noResultFoundIv?.setImageDrawable(
                            context?.let {
                                ContextCompat.getDrawable(
                                    it,
                                    R.drawable.empty_my_classified
                                )
                            }
                        )
                        binding?.emptyTextVew?.text = "You haven't listed anything yet"
                        binding?.recyclerViewAdvertise?.visibility = View.GONE
                    } else {
                        binding?.searchView?.isEnabled = true
                        /*response.data?.forEach {
                            if (!advertiseIdList.contains(it.advertisementId)) {
                                advertiseIdList.add(it.advertisementId)
                            }
                        }*/
                        response.data?.let { advertiseAdapter?.setData(it) }
                        response.data?.let { data = it }
                        //response.data?.let { searchAdvertisement(it) }
                        binding?.noResultFound?.visibility = View.GONE
                        binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                    }


                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Error ${response.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                }
            }
        }

        advertiseViewModel.advertiseDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE

                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    /* Toast.makeText(
                         context,
                         "${response.data?.advertisementDetails?.adImage}.",
                         Toast.LENGTH_SHORT
                     ).show()*/
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        advertiseViewModel.cancelAdvertiseData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        advertiseViewModel.callAdvertiseApiAfterCancel(true)
                        advertiseViewModel.cancelAdvertiseData.postValue(null)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }

        advertiseViewModel.searchQueryFromHomeScreen.observe(viewLifecycleOwner) { editable ->
            advertisementList.clear()
            val searchText = editable.toString().lowercase(Locale.getDefault())
            if (searchText.isNotEmpty()) {
                data?.forEach {
                    if (it.advertisementDetails.adTitle.lowercase(Locale.getDefault())
                            .contains(searchText)
                    ) {
                        advertisementList.add(it)
                    }
                }
            } else {
                advertisementList.clear()
                data.let {
                    if (it != null) {
                        advertisementList.addAll(it)
                    }
                }
            }
            if (isUserLogin == true) {
                if (advertisementList.isEmpty() ) {
                    if (binding?.searchView?.text.toString().isNotEmpty()){
                        binding?.noResultFound?.visibility = View.VISIBLE
                        binding?.noResultFoundIv?.setImageDrawable(
                            context?.let {
                                ContextCompat.getDrawable(
                                    it,
                                    R.drawable.no_immigration_found
                                )
                            }
                        )
                        binding?.emptyTextVew?.text = "Results not found"
                        binding?.recyclerViewAdvertise?.visibility = View.GONE
                    }
                } else {
                    binding?.noResultFound?.visibility = View.GONE
                    binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                }
            }

            advertiseAdapter?.setData(advertisementList)
            binding?.recyclerViewAdvertise?.adapter?.notifyDataSetChanged()
        }

        if (isUserLogin == false) {
            binding?.searchView?.isEnabled = false
            binding?.searchView?.isFocusable = false
            binding?.searchView?.isFocusableInTouchMode = false
            binding?.noResultFound?.visibility = View.GONE
        }

        /*if (advertiseIdList.isNotEmpty()) {
            advertiseIdList.forEachIndexed { index, i ->
                if (index == 0) {
                    //advertiseViewModel.getAdvertiseDetailsById(i)
                }
            }
        }*/

        return binding?.root
    }


    /*@SuppressLint("NotifyDataSetChanged")
    private fun searchAdvertisement(data: List<AllAdvertiseResponseItem>) {
        binding?.searchView?.addTextChangedListener { editable ->
            advertisementList.clear()
            val searchText = editable.toString().lowercase(Locale.getDefault())
            if (searchText.isNotEmpty()) {
                data.forEach {
                    if (it.advertisementDetails.adTitle.lowercase(Locale.getDefault())
                            .contains(searchText)
                    ) {
                        advertisementList.add(it)
                    }
                }
            } else {
                advertisementList.clear()
                data.let { advertisementList.addAll(it) }
            }
            if (advertisementList.isEmpty()) {
                binding?.noResultFound?.visibility = View.VISIBLE
                binding?.emptyTextVew?.text = "Results not found"
                binding?.recyclerViewAdvertise?.visibility = View.GONE
            } else {
                binding?.noResultFound?.visibility = View.GONE
                binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
            }
            advertiseAdapter?.setData(advertisementList)
            binding?.recyclerViewAdvertise?.adapter?.notifyDataSetChanged()
        }
    }*/

    private fun getCalculatedDate(dateFormat: String?, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}