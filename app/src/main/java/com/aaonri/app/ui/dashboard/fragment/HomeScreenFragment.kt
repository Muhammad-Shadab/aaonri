package com.aaonri.app.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.home.model.InterestResponseItem
import com.aaonri.app.data.home.viewmodel.HomeViewModel
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.data.jobs.recruiter.model.SearchAllTalentRequest
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.data.jobs.seeker.model.AllJobsResponseItem
import com.aaonri.app.data.jobs.seeker.viewmodel.JobSeekerViewModel
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.ClassifiedGenericAdapter
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventGenericAdapter
import com.aaonri.app.ui.dashboard.fragment.homescreen_filter.adapter.SearchFilterModuleAdapter
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.AllJobProfileAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobSeekerAdapter
import com.aaonri.app.ui.dashboard.home.adapter.HomeInterestsServiceAdapter
import com.aaonri.app.ui.dashboard.home.adapter.InterestAdapter
import com.aaonri.app.ui.dashboard.home.adapter.PoplarClassifiedAdapter
import com.aaonri.app.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    var binding: FragmentHomeScreenBinding? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    val jobSeekerViewModel: JobSeekerViewModel by activityViewModels()

    var adsGenericAdapter1: AdsGenericAdapter? = null
    var adsGenericAdapter2: AdsGenericAdapter? = null
    var allJobProfileAdapter: AllJobProfileAdapter? = null

    private var interestServiceResponseItem: InterestResponseItem? = null

    //var allClassifiedAdapter: AllClassifiedAdapter? = null
    //var allClassifiedAdapterForHorizontal: AllClassifiedAdapter? = null
    var popularClassifiedAdapter: PoplarClassifiedAdapter? = null
    var homeInterestsServiceAdapter: HomeInterestsServiceAdapter? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    var immigrationAdapter: ImmigrationAdapter? = null
    var jobSeekerAdapter: JobSeekerAdapter? = null
    var interestAdapter: InterestAdapter? = null

    //var homeEventAdapter: HomeEventAdapter? = null
    var genericAdapterForClassified: ClassifiedGenericAdapter? = null
    var genericAdapterForEvent: EventGenericAdapter? = null
    var searchFilterModuleAdapter: SearchFilterModuleAdapter? = null
    var priorityService = ""
    var navigationFromHorizontalSeeAll = ""
    var userInterestedService: MutableList<String>? = mutableListOf()
    var guestUser = false
    var isJobRecruiter = false
    var navigateBackToCloseApp = true

    var homeClassifiedWithAdList = mutableListOf<Any>()

    //var homeEventWithAdList = mutableListOf<Any>()
    private lateinit var layoutManager2: LinearLayoutManager
    private lateinit var layoutManager1: LinearLayoutManager
    var adRvposition1 = 0
    var adRvposition2 = 0
    var timer1: Timer? = null
    var timerTask1: TimerTask? = null
    var timer2: Timer? = null
    var timerTask2: TimerTask? = null

    var jobId = 0
    var advertiseId = 0
    var classifiedId = 0
    var eventId = 0
    var immigrationId = 0
    var shopWithUsId = 0
    var astrologyId = 0
    var businessNeedId = 0
    var communityConnectId = 0
    var foundationAndDonationId = 0
    var homeNeedId = 0
    var legalServicesId = 0
    var matrimonyId = 0
    var medicalCareId = 0
    var realStateId = 0
    var sports = 0
    var studentService = 0
    var travelStay = 0

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        if (BuildConfig.FLAVOR == "dev") {
            advertiseId = 27
            classifiedId = 2
            eventId = 8
            immigrationId = 3
            jobId = 17
            shopWithUsId = 22
            astrologyId = 4
            businessNeedId = 26
            communityConnectId = 10
            foundationAndDonationId = 13
            homeNeedId = 25
            legalServicesId = 18
            matrimonyId = 19
            medicalCareId = 20
            realStateId = 21
            sports = 5
            studentService = 16
            travelStay = 24
        } else {
            advertiseId = 19
            classifiedId = 1
            eventId = 2
            immigrationId = 4
            jobId = 3
            shopWithUsId = 15
            astrologyId = 5
            businessNeedId = 18
            communityConnectId = 8
            foundationAndDonationId = 9
            homeNeedId = 17
            legalServicesId = 11
            matrimonyId = 12
            medicalCareId = 13
            realStateId = 14
            sports = 6
            studentService = 10
            travelStay = 16
        }

        val userCity = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        isJobRecruiter =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_JOB_RECRUITER, false] } == true

        val isUserLogin =
            context.let {
                it?.let { it1 -> PreferenceManager<Boolean>(it1) }
                    ?.get(Constant.IS_USER_LOGIN, false)
            }

        val list =
            context?.let { PreferenceManager<String>(it)[Constant.USER_INTERESTED_SERVICES, ""] }
                .toString()

        if (list.isNotEmpty()) {
            userInterestedService = list.split(",") as MutableList<String>?
        }

        /** Removing unnecessary User Interest service id **/
        if (userInterestedService?.size != null) {
            if (userInterestedService?.contains("$shopWithUsId") == true) {
                userInterestedService?.remove("$shopWithUsId")
            }
            if (userInterestedService?.contains("$advertiseId") == true) {
                userInterestedService?.remove("$advertiseId")
            }
            /* if (userInterestedService?.contains("$jobId") == true) {
                 userInterestedService?.remove("$jobId")
             }*/

            if (userInterestedService?.size == 1) {
                if (userInterestedService?.contains("$classifiedId") == false) {
                    userInterestedService?.add("$classifiedId")
                }
            }
            if (userInterestedService?.size == 1) {
                if (userInterestedService?.contains("$eventId") == false) {
                    userInterestedService?.add("$eventId")
                }
            }
        }

        /*allClassifiedAdapter = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }*/

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
            Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).circleCrop().error(R.drawable.profile_pic_placeholder)
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

        closeDialogBtn.setOnClickListener {
            updateLogoutDialog.dismiss()
        }

        editProfileBtn.setOnClickListener {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToUpdateProfileFragment()
            findNavController().navigate(action)
            updateLogoutDialog.dismiss()
        }

        /**  Dialog for guest user **/
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

        genericAdapterForClassified = ClassifiedGenericAdapter()
        genericAdapterForEvent = EventGenericAdapter()
        adsGenericAdapter1 = AdsGenericAdapter()
        adsGenericAdapter2 = AdsGenericAdapter()


        if (isJobRecruiter) {
            jobRecruiterViewModel.getAllTalents(
                SearchAllTalentRequest(
                    allKeyWord = "",
                    anyKeyWord = "",
                    availability = "",
                    location = "",
                    skill = ""
                )
            )
        } else {
            jobSeekerViewModel.getAllActiveJobs()
        }

        /** call back function for getting the ads clicked item **/
        adsGenericAdapter1?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToAdvertiseWebviewFragment(
                        item.advertisementDetails.url
                    )
                findNavController().navigate(action)
            }
        }

        /** call back function for getting the ads clicked item **/
        adsGenericAdapter2?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToAdvertiseWebviewFragment(
                        item.advertisementDetails.url
                    )
                findNavController().navigate(action)
            }
        }

        /** This generic adapter can be used with the multiple view type so that we can add the advertise item between the classified items **/
        genericAdapterForClassified?.itemClickListener = { view, item, position ->
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    item.id,
                    false
                )
            findNavController().navigate(action)
        }

        /** This generic adapter can be used with the multiple view type so that we can add the advertise item between the classified items **/
        genericAdapterForEvent?.itemClickListenerEvent = { view, item, position ->
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(
                    item.id
                )
            findNavController().navigate(action)
        }

        /*homeEventAdapter = HomeEventAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToEventDetailsScreenFragment(it.id)
            findNavController().navigate(action)
        }*/

        /** This adapter is used for showing advertise on home screen  **/
        /*advertiseAdapter = AdvertiseAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToAdvertisementDetailsFragment(
                    it.advertisementId
                )
            findNavController().navigate(action)
        }*/

        /** This adapter is used for showing job on home screen  **/
        jobSeekerAdapter = JobSeekerAdapter()



        jobSeekerAdapter?.itemClickListener = { view, item, position ->

            /** Getting user job profile information to apply jobs **/
            jobSeekerViewModel.getUserJobProfileByEmail(
                emailId = email ?: "",
                isApplicant = true
            )

            if (item is AllJobsResponseItem) {
                if (isUserLogin == true) {
                    if (view.id == R.id.jobCv) {
                        /** Clicked on Job Card View **/
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToJobDetailsFragment(
                                item.jobId,
                                false
                            )
                        findNavController().navigate(action)
                    } else {
                        /** Clicked on Apply btn **/
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToJobApplyFragment(
                                item.jobId, false
                            )
                        findNavController().navigate(action)
                    }
                } else {
                    guestUserLoginDialog.show()
                }
            }
        }

        allJobProfileAdapter = AllJobProfileAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToRecruiterTalentDetailsFragment(
                    it.id
                )
            findNavController().navigate(action)
        }

        searchFilterModuleAdapter = SearchFilterModuleAdapter {
            interestServiceResponseItem = it
        }

        /*allClassifiedAdapterForHorizontal = AllClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }*/

        immigrationAdapter = ImmigrationAdapter()

        immigrationAdapter?.itemClickListener =
            { view, item, position, updateImmigration, deleteImmigration ->
                if (item is Discussion) {
                    val action =
                        HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationDetailsFragment(
                            true,
                            0
                        )
                    findNavController().navigate(action)
                    immigrationViewModel.setSelectedDiscussionItem(item)
                }
            }

        //jobAdapter?.setData(listOf("Test 1", "Test 2", "Test 3", "Test 4"))

        /** This callback is used for navigating to the specific screen from the  top horizontal services row in homescreen**/
        interestAdapter = InterestAdapter {
            navigateToTheSpecificScreen(it.interestDesc)
        }

        /** This adapter is used for showing Horizontal tab of user selected services **/
        homeInterestsServiceAdapter =
            HomeInterestsServiceAdapter {
                /** This callback is used for changing the selected horizontal tab **/
                classifiedViewModel.setSelectedServiceRow(it)
                binding?.eventTv?.text = it
                /*if (it == "Shop With Us") {
                    homeScreenBinding?.availableServiceHorizontalClassifiedRv?.visibility =
                        View.GONE
                    homeScreenBinding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                    dashboardCommonViewModel.setIsShopWithUsClicked(true)
                } else {
                    homeScreenBinding?.eventTv?.text = it
                }*/
                navigationFromHorizontalSeeAll = it
                when (it) {
                    "Classifieds" -> {
                        var itemDecoration: RecyclerView.ItemDecoration? = null
                        while (binding?.availableServiceHorizontalClassifiedRv?.itemDecorationCount!! > 0 && (binding?.availableServiceHorizontalClassifiedRv?.getItemDecorationAt(
                                0
                            )?.let { itemDecoration = it }) != null
                        ) {
                            itemDecoration?.let { it1 ->
                                binding?.availableServiceHorizontalClassifiedRv?.removeItemDecoration(
                                    it1
                                )
                            }
                        }
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.VISIBLE
                        binding?.availableServiceHorizontalRv?.visibility = View.GONE
                        binding?.availableServiceHorizontalClassifiedRv?.layoutManager =
                            GridLayoutManager(context, 2)
                        binding?.availableServiceHorizontalClassifiedRv?.addItemDecoration(
                            GridSpacingItemDecoration(
                                2,
                                32,
                                40
                            )
                        )
                        /*homeScreenBinding?.availableServiceHorizontalClassifiedRv?.adapter =
                            allClassifiedAdapterForHorizontal*/
                        binding?.availableServiceHorizontalClassifiedRv?.adapter =
                            genericAdapterForClassified
                    }
                    "Events" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(10F, 0f, 0F, 0F)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        //homeScreenBinding?.availableServiceHorizontalRv?.adapter = homeEventAdapter
                        binding?.availableServiceHorizontalRv?.adapter =
                            genericAdapterForEvent
                    }
                    "Jobs" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility = View.GONE

                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 10F)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                        if (isJobRecruiter) {
                            binding?.availableServiceHorizontalRv?.adapter = allJobProfileAdapter
                        } else {
                            binding?.availableServiceHorizontalRv?.adapter = jobSeekerAdapter
                        }
                    }
                    "Immigration" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 4F)

                        /* val userArray =
                             JSONObject(loadJSONFromAsset()).getJSONArray("immigrationcenterlist")
                         val gson = Gson()

                         for (i in 0 until userArray.length()) {
                             if (!immigrationList.contains(
                                     gson.fromJson(
                                         userArray.getString(i),
                                         ImmigrationCenterModelItem::class.java
                                     )
                                 )
                             ) {
                                 immigrationList.add(
                                     gson.fromJson(
                                         userArray.getString(i),
                                         ImmigrationCenterModelItem::class.java
                                     )
                                 )
                             }
                         }*/

                        immigrationViewModel.getAllImmigrationDiscussion(
                            GetAllImmigrationRequest(
                                categoryId = "1",
                                createdById = "",
                                keywords = ""
                            )
                        )

                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding?.availableServiceHorizontalRv?.adapter = immigrationAdapter
                    }
                    "Astrology" -> {

                    }
                    "Sports" -> {

                    }
                    "Community Connect" -> {

                    }
                    "Foundation & Donations" -> {

                    }
                    "Student Services" -> {

                    }
                    "Legal Services" -> {

                    }
                    "Matrimony & Weddings" -> {

                    }
                    "Medical Care" -> {

                    }
                    "Real Estate" -> {

                    }
                    "Travel and Stay" -> {

                    }
                    "Home Needs" -> {

                    }
                    "Business Needs" -> {

                    }
                }
            }

        popularClassifiedAdapter = PoplarClassifiedAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedDetailsFragment(
                    it.id,
                    false
                )
            findNavController().navigate(action)
        }

        binding?.apply {

            loginBtn.setOnClickListener {
                activity?.finish()
            }

            dismissBtn.setOnClickListener {
                guestUserLoginDialog.dismiss()
            }

            if (userCity != null) {
                if (userCity.isNotEmpty()) {
                    locationTv.text = userCity
                    locationTv.visibility = View.VISIBLE
                    locationIcon.visibility = View.VISIBLE
                } else {
                    locationTv.visibility = View.GONE
                    locationIcon.visibility = View.GONE
                }
            }

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).centerCrop().error(R.drawable.profile_pic_placeholder)
                    .into(profilePicIv)
            }

            seeAllTv1.setOnClickListener {
                if (isUserLogin == true) {
                    navigateToTheSpecificScreen(userInterestedService?.get(0))
                } else {
                    navigateToTheSpecificScreen("$classifiedId")
                }
            }

            seeAllTv2.setOnClickListener {
                navigateToTheSpecificScreen(navigationFromHorizontalSeeAll)
            }

            profilePicCv.setOnClickListener {
                if (!guestUser) {
                    updateLogoutDialog.show()
                } else {
                    guestUserLoginDialog.show()
                }
            }


            cancelButton.setOnClickListener {
                searchView.setText("")
            }

            searchViewll.setOnClickListener {
                navigateBackToCloseApp = false
                dashboardCommonViewModel.setShowBottomNavigation(false)
                searchView.requestFocus()
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )

                searchModuleFl.visibility = View.VISIBLE
                /*blurView.setBlurRadius(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        25f,
                        resources.displayMetrics
                    )
                )*/
                stopAutoScrollBanner2()
                stopAutoScrollBanner1()

            }

            searchView.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                navigateBackToCloseApp = false
                dashboardCommonViewModel.setShowBottomNavigation(false)
                searchView.requestFocus()
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )

                searchModuleFl.visibility = View.VISIBLE
                /*blurView.setBlurRadius(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        25f,
                        resources.displayMetrics
                    )
                )*/
                stopAutoScrollBanner2()
                stopAutoScrollBanner1()
                return@OnTouchListener true
            })

            searchView.addTextChangedListener { editable ->
                if (searchView.hasFocus()) {
                    navigateBackToCloseApp = false
                    searchModuleFl.visibility = View.VISIBLE
                }
                if (searchView.text.toString().isNotEmpty()) {
                    cancelButton.visibility = View.VISIBLE
                    searchViewIcon.visibility = View.GONE
                } else {
                    context?.let { it1 -> PreferenceManager<Int>(it1) }
                        ?.set("selectedSearchModule", -1)
                    cancelButton.visibility = View.GONE
                    searchViewIcon.visibility = View.VISIBLE
                }
                /* if (searchView.hasFocus()) {
                     if (editable.toString().isNotEmpty()) {
                         searchModuleFl.visibility = View.VISIBLE
                     } else {
                         searchModuleFl.visibility = View.GONE
                     }
                 }*/
            }

            closeSearchScreen.setOnClickListener {
                navigateBackToCloseApp = true
                interestServiceResponseItem = null
                context?.let { it1 -> PreferenceManager<Int>(it1) }
                    ?.set("selectedSearchModule", -1)
                searchFilterModuleAdapter?.notifyDataSetChanged()
                dashboardCommonViewModel.setShowBottomNavigation(true)
                searchView.clearFocus()
                searchModuleFl.visibility = View.GONE
                searchView.setText("")
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                runAutoScrollBanner1()
                runAutoScrollBanner2()
            }

            searchBtn.setOnClickListener {
                dashboardCommonViewModel.setShowBottomNavigation(true)
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                searchQuery()
            }

            searchView.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    dashboardCommonViewModel.setShowBottomNavigation(true)
                    searchQuery()
                }
                false
            }


            /*seeAllPopularItems.setOnClickListener {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToClassifiedScreenFragment()
                findNavController().navigate(action)
            }*/

            interestRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            interestRecyclerView.adapter = interestAdapter

            activeService.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            activeService.adapter = homeInterestsServiceAdapter

            /*availableServiceHorizontalRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)*/

            popularItemsRv.layoutManager = GridLayoutManager(context, 2)
            popularItemsRv.addItemDecoration(GridSpacingItemDecoration(2, 32, 40))

            adsAbovePopularSectionRv.adapter = adsGenericAdapter2
            layoutManager2 = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
            adsAbovePopularSectionRv.layoutManager = layoutManager2
            adsAbovePopularSectionRv.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    32, 0
                )
            )


            adsBelowFirstSectionRv.adapter = adsGenericAdapter1
            layoutManager1 = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
            adsBelowFirstSectionRv.layoutManager = layoutManager1
            adsBelowFirstSectionRv.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    32, 0
                )
            )

            availableModuleSearchRv.adapter = searchFilterModuleAdapter
            availableModuleSearchRv.layoutManager = GridLayoutManager(context, 3)

            adsAbovePopularSectionRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == 1) {
                        stopAutoScrollBanner2()
                    } else if (newState == 0) {

                        adRvposition2 = layoutManager2.findFirstCompletelyVisibleItemPosition()
                        runAutoScrollBanner2()
                    }
                }
            })

            adsBelowFirstSectionRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == 1) {
                        stopAutoScrollBanner1()
                    } else if (newState == 0) {

                        adRvposition1 = layoutManager1.findFirstCompletelyVisibleItemPosition()
                        runAutoScrollBanner1()
                    }
                }
            })

            /**Used to scroll home to the top**/
            homeViewModel.homeContentScrollToTop.observe(viewLifecycleOwner) {
                if (it) {
                    homeNestedScrollView.post {
                        homeNestedScrollView.fling(0)
                        homeNestedScrollView.smoothScrollTo(0, 0)
                        homeViewModel.setHomeContentScrollToTop(false)
                    }

                }
            }
        }


        homeViewModel.homeEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE

                    if (response.data?.userEvent?.isNotEmpty() == true) {
                        if (response.data.userEvent.size >= 4) {
                            genericAdapterForEvent?.items = response.data.userEvent.subList(0, 4)
                            //homeEventAdapter?.setData(response.data.userEvent.subList(0, 4))
                        } else {
                            genericAdapterForEvent?.items = response.data.userEvent
                            //homeEventAdapter?.setData(response.data.userEvent)
                        }
                        //homeEventWithAdList = response.data.userEvent.subList(0, 4).toMutableList()
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        registrationViewModel.findByEmailData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    val list: MutableList<String>? =
                        response.data?.interests?.split(",") as MutableList<String>?

                    /** Removing unnecessary User Interest service id **/
                    if (list?.contains("$shopWithUsId") == true) {
                        list.remove("$shopWithUsId")
                    }
                    if (list?.contains("$advertiseId") == true) {
                        list.remove("$advertiseId")
                    }

                    /*if (list?.contains("$jobId") == true) {
                        list.remove("$jobId")
                    }*/

                    if (list != null) {
                        if (list.size == 1) {
                            if (!list.contains("$classifiedId")) {
                                list.add("$classifiedId")
                            }
                        }
                        if (list.size == 1) {
                            if (!list.contains("$eventId")) {
                                list.add("$eventId")
                            }
                        }
                    }

                    callApiAccordingToInterest(list?.get(0))

                    /** first interest will be removed and shows remaining interest in horizontal tab row **/
                    list?.removeAt(0)
                    setUserInterestedServiceRow(list)
                }
                is Resource.Error -> {

                }
                else -> {
                }
            }
        }
        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            guestUser = it
            if (it) {
                setHomeClassifiedData()
                binding?.classifiedTv?.visibility = View.VISIBLE
                setUserInterestedServiceRow()
            }
        }

        homeViewModel.popularClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.homeConstraintLayout?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        if (response.data.size > 6) {
                            popularClassifiedAdapter?.setData(response.data.subList(0, 6))
                        } else {
                            popularClassifiedAdapter?.setData(response.data)
                        }
                    }
                    binding?.homeConstraintLayout?.visibility = View.VISIBLE
                    binding?.popularItemsRv?.adapter = popularClassifiedAdapter
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.homeConstraintLayout?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        classifiedViewModel.classifiedForHomeScreen.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    response.data?.userAdsList?.let {
                        if (classifiedViewModel.allClassifiedList.isEmpty()) {
                            classifiedViewModel.setClassifiedForHomeScreen(it)
                        }
                    }

                    /*if (classifiedViewModel.allClassifiedList.size > 3) {
                        *//*allClassifiedAdapter?.setData(
                            classifiedViewModel.allClassifiedList.subList(
                                0,
                                4
                            )
                        )*//*
                        homeClassifiedWithAdList =
                            classifiedViewModel.allClassifiedList.subList(0, 4).toMutableList()

                        //genericAdapter?.items = classifiedViewModel.allClassifiedList.subList(0, 4)
                        *//*allClassifiedAdapterForHorizontal?.setData(
                            classifiedViewModel.allClassifiedList.subList(
                                0,
                                4
                            )
                        )*//*
                    } else {
                        //genericAdapter?.items = classifiedViewModel.allClassifiedList
                        //allClassifiedAdapter?.setData(classifiedViewModel.allClassifiedList)
                        //allClassifiedAdapterForHorizontal?.setData(classifiedViewModel.allClassifiedList)
                    }*/
                    homeClassifiedWithAdList = classifiedViewModel.allClassifiedList.toMutableList()
                    genericAdapterForClassified?.items =
                        if (response.data?.userAdsList?.size!! >= 4) response.data.userAdsList.subList(
                            0,
                            4
                        ) else response.data.userAdsList
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        homeViewModel.homeClassifiedInlineAds.observe(viewLifecycleOwner) {
            if (homeClassifiedWithAdList.size >= 4) {
                homeClassifiedWithAdList.add(index = 2, it)
            } else {
                homeClassifiedWithAdList.add(it)
            }
            genericAdapterForClassified?.items = homeClassifiedWithAdList
        }

        /*homeViewModel.homeEventInlineAds.observe(viewLifecycleOwner) {
            if (homeEventWithAdList.size >= 4) {
                homeEventWithAdList.add(index = 2, it)
            } else {
                homeEventWithAdList.add(it)
            }
            genericAdapterForEvent?.items = homeEventWithAdList
        }*/

        homeViewModel.adsBelowFirstSection.observe(viewLifecycleOwner) {
            adsGenericAdapter1?.items = it
            binding?.adsBelowFirstSectionRv?.visibility = View.VISIBLE
            runAutoScrollBanner1()
        }

        homeViewModel.adsAbovePopularItem.observe(viewLifecycleOwner) {
            adsGenericAdapter2?.items = it
            binding?.adsAbovePopularSectionRv?.visibility = View.VISIBLE
            runAutoScrollBanner2()
        }

        immigrationViewModel.allImmigrationDiscussionListData.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.discussionList?.let {
                        if (it.size >= 4) {
                            immigrationAdapter?.setData(it.subList(0, 4))
                        } else {
                            immigrationAdapter?.setData(it)
                        }
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        /**Job recruiter data**/
        jobRecruiterViewModel.allTalentListData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    response.data?.let {
                        if (it.jobProfiles.isNotEmpty()) {
                            if (it.jobProfiles.size >= 4) {
                                allJobProfileAdapter?.setData(
                                    it.jobProfiles.filter { it.isApplicant }.subList(0, 4)
                                )
                            } else {
                                allJobProfileAdapter?.setData(it.jobProfiles.filter { it.isApplicant })
                            }
                        }
                    }
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        /**Job seeker data**/
        jobSeekerViewModel.allActiveJobsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let {
                        if (it.isNotEmpty()) {
                            if (it.size >= 4) {
                                jobSeekerAdapter?.setData(it.subList(0, 4))
                            } else {
                                jobSeekerAdapter?.setData(it)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }

        }

        /*requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navigateBackToCloseApp) {
                        activity?.finish()
                    }
                }
            })*/

        return binding?.root
    }

    private fun searchQuery() {
        if (binding?.searchView?.text?.toString()?.isNotEmpty() == true) {
            if (interestServiceResponseItem?.id == classifiedId) {
                classifiedViewModel.setSearchQueryFromHomeScreen(binding?.searchView?.text.toString())
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
                binding?.searchView?.setText("")
                interestServiceResponseItem = null
            } else if (interestServiceResponseItem?.id == immigrationId) {
                val bundle = Bundle()
                bundle.putString("searchKeyword", binding?.searchView?.text.toString())
                findNavController().navigate(
                    R.id.action_homeScreenFragment_to_immigrationScreenFragment,
                    bundle
                )
                binding?.searchView?.setText("")
                interestServiceResponseItem = null
            } else if (interestServiceResponseItem?.id == eventId) {
                val bundle = Bundle()
                bundle.putString("searchKeyword", binding?.searchView?.text.toString())
                findNavController().navigate(
                    R.id.action_homeScreenFragment_to_eventScreenFragment,
                    bundle
                )
                binding?.searchView?.setText("")
                interestServiceResponseItem = null
            } else if (interestServiceResponseItem?.id == advertiseId) {
                dashboardCommonViewModel.setIsAdvertiseClicked(true)
                advertiseViewModel.setSearchQueryToSetOnSearchViewValue(binding?.searchView?.text.toString())
                binding?.searchView?.setText("")
                interestServiceResponseItem = null
            } else {
                activity?.let { it1 ->
                    Snackbar.make(
                        it1.findViewById(android.R.id.content),
                        "Please select one of the options to continue", Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            activity?.let { it1 ->
                Snackbar.make(
                    it1.findViewById(android.R.id.content),
                    "Please enter valid keyword", Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    /** showing all the selected services horizontal tab for both login user and guest user**/
    private fun setUserInterestedServiceRow(interests: MutableList<String>? = null) {
        homeViewModel.allInterestData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val activeServiceList = mutableListOf<InterestResponseItem>()
                    if (response.data?.isNotEmpty() == true) {
                        searchFilterModuleAdapter?.setData(response.data.filter { it.id == immigrationId || it.id == classifiedId || it.id == eventId || (it.id == advertiseId && !guestUser) })
                        if (interests?.isNotEmpty() == true) {
                            response.data.forEach {
                                if (!activeServiceList.contains(it) && it.active && interests.contains(
                                        it.id.toString()
                                    ) && it.interestDesc != "Advertise With Us"
                                ) {
                                    activeServiceList.add(it)
                                }
                            }
                        }
                        binding?.interestRecyclerView?.visibility = View.VISIBLE
                        binding?.interestBorder?.visibility = View.VISIBLE
                        interestAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" /*&& it.id != jobId*/ })
                        if (interests.isNullOrEmpty()) {
                            /**This will show all active interested services in guest user**/
                            homeInterestsServiceAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" && it.id == eventId } as MutableList<InterestResponseItem>)
                        } else {
                            homeInterestsServiceAdapter?.setData(activeServiceList)
                        }
                    }
                }
                is Resource.Error -> {

                }
            }
        }
    }

    private fun navigateToTheSpecificScreen(interests: String?) {
        if (interests?.isNotEmpty() == true) {
            if (interests == "$advertiseId" || interests == "Advertise With Us") {
                //Advertise With Us
                dashboardCommonViewModel.setIsAdvertiseClicked(true)
            } else if (interests == "$classifiedId" || interests == "Classifieds") {
                //Classifieds
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
            } else if (interests == "$eventId" || interests == "Events") {
                //Events
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            } else if (interests == "$immigrationId" || interests == "Immigration") {
                //Immigration
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationScreenFragment()
                findNavController().navigate(action)
            } else if (interests == "$jobId" || interests == "Jobs") {
                //Jobs

                if (guestUser) {
                    val action =
                        HomeScreenFragmentDirections.actionHomeScreenFragmentToJobScreenFragment()
                    findNavController().navigate(action)
                } else {
                    if (isJobRecruiter) {
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToJobRecruiterScreenFragment()
                        findNavController().navigate(action)
                    } else {
                        val action =
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToJobScreenFragment()
                        findNavController().navigate(action)
                    }
                }


            } else if (interests == "$shopWithUsId" || interests == "Shop With Us") {
                //Shop With Us
                dashboardCommonViewModel.setIsShopWithUsClicked(true)
            } else if (interests == "$astrologyId" || interests == "Astrology") {
                //Astrology

            } else if (interests == "$businessNeedId" || interests == "Business Needs") {
                //Business Needs

            } else if (interests == "$communityConnectId" || interests == "Community Connect") {
                //Community Connect

            } else if (interests == "$foundationAndDonationId" || interests == "Foundation & Donations") {
                //Foundation & Donations

            } else if (interests == "$homeNeedId" || interests == "Home Needs") {
                //Home Needs

            } else if (interests == "$legalServicesId" || interests == "Legal Services") {
                //Legal Services

            } else if (interests == "$matrimonyId" || interests == "Matrimony & Weddings") {
                //Matrimony & Weddings

            } else if (interests == "$medicalCareId" || interests == "Medical Care") {
                //Medical Care

            } else if (interests == "$realStateId" || interests == "Real Estate") {
                //Real Estate

            } else if (interests == "$sports" || interests == "Sports") {
                //Sports

            } else if (interests == "$studentService" || interests == "Student Services") {
                //Student Services

            } else if (interests == "$travelStay" || interests == "Travel and Stay") {
                //Travel and Stay

            }
        }
    }

    /** This function is used to set first selected service data to the top **/

    private fun callApiAccordingToInterest(
        interests: String? = "",
    ) {
        if (interests?.isNotEmpty() == true) {
            if (interests == "$advertiseId") {
                //Advertise With Us
                priorityService = "Advertise With Us"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = advertiseAdapter

            } else if (interests == "$classifiedId") {
                //Classifieds
                priorityService = "Classifieds"
                setHomeClassifiedData()
            } else if (interests == "$eventId") {
                //Events
                priorityService = "Events"
                binding?.priorityServiceRv?.margin(left = 20f, right = 14f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                //homeScreenBinding?.priorityServiceRv?.adapter = homeEventAdapter
                binding?.priorityServiceRv?.adapter = genericAdapterForEvent

            } else if (interests == "$immigrationId") {
                //Immigration
                priorityService = "Immigration"
                binding?.priorityServiceRv?.margin(left = 16f, right = 16f)
                immigrationViewModel.getAllImmigrationDiscussion(
                    GetAllImmigrationRequest(
                        categoryId = "1",
                        createdById = "",
                        keywords = ""
                    )
                )
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding?.priorityServiceRv?.adapter = immigrationAdapter
            } else if (interests == "$jobId") {
                //Jobs
                priorityService = "Jobs"
                binding?.priorityServiceRv?.margin(left = 10f, right = 10f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                if (isJobRecruiter) {
                    binding?.priorityServiceRv?.adapter = allJobProfileAdapter
                } else {
                    binding?.priorityServiceRv?.adapter = jobSeekerAdapter
                }


            } else if (interests == "$shopWithUsId") {
                //Shop With Us

            } else if (interests == "$astrologyId") {
                //Astrology
                priorityService = "Astrology"


            } else if (interests == "$businessNeedId") {
                //Business Needs
                priorityService = "Business Needs"


            } else if (interests == "$communityConnectId") {
                //Community Connect
                priorityService = "Community Connect"

            } else if (interests == "$foundationAndDonationId") {
                //Foundation & Donations
                priorityService = "Foundation & Donations"

            } else if (interests == "$homeNeedId") {
                //Home Needs
                priorityService = "Home Needs"

            } else if (interests == "$legalServicesId") {
                //Legal Services
                priorityService = "Legal Services"

            } else if (interests == "$matrimonyId") {
                //Matrimony & Weddings
                priorityService = "Matrimony & Weddings"

            } else if (interests == "$medicalCareId") {
                //Medical Care
                priorityService = "Medical Care"

            } else if (interests == "$realStateId") {
                //Real Estate
                priorityService = "Real Estate"

            } else if (interests == "$sports") {
                //Sports
                priorityService = "Sports"

            } else if (interests == "$studentService") {
                //Student Services
                priorityService = "Student Services"

            } else if (interests == "$travelStay") {
                //Travel and Stay
                priorityService = "Travel and Stay"
            }
        }
        binding?.classifiedTv?.text = priorityService
        binding?.classifiedTv?.visibility = View.VISIBLE
        /*if (interests?.isNotEmpty() == true && interests.contains("22")) {
            homeScreenBinding?.classifiedTv?.visibility = View.GONE
            homeScreenBinding?.seeAllClassified?.visibility = View.GONE
            homeScreenBinding?.adsBelowFirstSectionRv?.margin(top = 8f)
        } else {
            homeScreenBinding?.seeAllClassified?.visibility = View.VISIBLE
            homeScreenBinding?.classifiedTv?.text = priorityService
            homeScreenBinding?.classifiedTv?.visibility = View.VISIBLE
        }*/
    }


    private fun setHomeClassifiedData() {

        binding?.priorityServiceRv?.layoutManager =
            GridLayoutManager(context, 2)
        binding?.priorityServiceRv?.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                32,
                40
            )
        )
        //homeScreenBinding?.priorityServiceRv?.adapter = allClassifiedAdapter
        binding?.priorityServiceRv?.adapter = genericAdapterForClassified

    }


    fun View.margin(
        left: Float? = null,
        top: Float? = null,
        right: Float? = null,
        bottom: Float? = null
    ) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    override fun onResume() {
        super.onResume()
        runAutoScrollBanner2()
        runAutoScrollBanner1()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollBanner2()
        stopAutoScrollBanner1()
    }

    fun stopAutoScrollBanner1() {
        if (timer1 != null && timerTask1 != null) {
            timerTask1!!.cancel()
            timer1!!.cancel()
            timer1 = null
            timerTask1 = null
            adRvposition1 = layoutManager1.findFirstCompletelyVisibleItemPosition()
        }
    }

    fun runAutoScrollBanner1() {

        if (timer1 == null && timerTask1 == null && adsGenericAdapter1?.items?.size!! >= 3) {
            timer1 = Timer()
            timerTask1 = object : TimerTask() {

                override fun run() {

                    if (adRvposition1 == Int.MAX_VALUE) {
                        adRvposition1 = Int.MAX_VALUE / 2
                        binding?.adsBelowFirstSectionRv?.smoothScrollToPosition(adRvposition1)

                    } else {
                        adRvposition1 += 3
                        binding?.adsBelowFirstSectionRv?.smoothScrollToPosition(adRvposition1)
                    }
                }
            }
            timer1!!.schedule(timerTask1, 4000, 4000)
        }
    }

    fun stopAutoScrollBanner2() {
        if (timer2 != null && timerTask2 != null) {
            timerTask2!!.cancel()
            timer2!!.cancel()
            timer2 = null
            timerTask2 = null
            adRvposition2 = layoutManager2.findFirstCompletelyVisibleItemPosition()
        }
    }

    fun runAutoScrollBanner2() {
        if (timer2 == null && timerTask2 == null && adsGenericAdapter2?.items?.size!! >= 3) {
            timer2 = Timer()
            timerTask2 = object : TimerTask() {
                override fun run() {
                    if (adRvposition2 == Int.MAX_VALUE) {
                        adRvposition2 = Int.MAX_VALUE / 2
                        binding?.adsAbovePopularSectionRv?.smoothScrollToPosition(adRvposition2)

                    } else {
                        adRvposition2 += 3
                        binding?.adsAbovePopularSectionRv?.smoothScrollToPosition(adRvposition2)
                    }
                }
            }
            timer2!!.schedule(timerTask2, 4000, 4000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

