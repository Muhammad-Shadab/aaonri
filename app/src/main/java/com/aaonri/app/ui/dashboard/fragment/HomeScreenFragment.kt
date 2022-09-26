package com.aaonri.app.ui.dashboard.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentHomeScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.ClassifiedGenericAdapter
import com.aaonri.app.ui.dashboard.fragment.event.adapter.EventGenericAdapter
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter.JobSeekerAdapter
import com.aaonri.app.ui.dashboard.home.adapter.HomeInterestsServiceAdapter
import com.aaonri.app.ui.dashboard.home.adapter.InterestAdapter
import com.aaonri.app.ui.dashboard.home.adapter.PoplarClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.nio.charset.Charset
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
    var adsGenericAdapter1: AdsGenericAdapter? = null

    var adsGenericAdapter2: AdsGenericAdapter? = null

    //var allClassifiedAdapter: AllClassifiedAdapter? = null
    //var allClassifiedAdapterForHorizontal: AllClassifiedAdapter? = null
    var popularClassifiedAdapter: PoplarClassifiedAdapter? = null
    var homeInterestsServiceAdapter: HomeInterestsServiceAdapter? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    var immigrationAdapter: ImmigrationAdapter? = null
    var jobAdapter: JobSeekerAdapter? = null
    var interestAdapter: InterestAdapter? = null

    //var homeEventAdapter: HomeEventAdapter? = null
    var genericAdapterForClassified: ClassifiedGenericAdapter? = null
    var genericAdapterForEvent: EventGenericAdapter? = null
    val eventId = mutableListOf<Int>()
    var priorityService = ""
    var navigationFromHorizontalSeeAll = ""
    var userInterestedService: MutableList<String>? = mutableListOf()
    var guestUser = false
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        val dialog = Dialog(requireContext())

        val userCity = context?.let { PreferenceManager<String>(it)[Constant.USER_CITY, ""] }

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val userName =
            context?.let { PreferenceManager<String>(it)[Constant.USER_NAME, ""] }

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val list =
            context?.let { PreferenceManager<String>(it)[Constant.USER_INTERESTED_SERVICES, ""] }
                .toString()

        if (list.isNotEmpty()) {
            userInterestedService = list.split(",") as MutableList<String>?
        }

        /** Removing unnecessary User Interest service id **/
        if (userInterestedService?.size != null) {
            if (userInterestedService?.contains("22") == true) {
                userInterestedService?.remove("22")
            }
            if (userInterestedService?.contains("27") == true) {
                userInterestedService?.remove("27")
            }
            if (userInterestedService?.contains("17") == true) {
                userInterestedService?.remove("17")
            }

            if (userInterestedService?.size == 1) {
                if (userInterestedService?.contains("2") == false) {
                    userInterestedService?.add("2")
                }
            }
            if (userInterestedService?.size == 1) {
                if (userInterestedService?.contains("8") == false) {
                    userInterestedService?.add("8")
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
        dialog.setContentView(R.layout.update_profile_dialog)
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )

        dialog.setCancelable(false)
        val editProfileBtn =
            dialog.findViewById<TextView>(R.id.editProfileBtn)
        val logOutBtn =
            dialog.findViewById<TextView>(R.id.logOutBtn)
        val closeDialogBtn =
            dialog.findViewById<ImageView>(R.id.closeDialogBtn)
        val dialogProfileIv =
            dialog.findViewById<ImageView>(R.id.profilePicIv)
        val userNameTv =
            dialog.findViewById<TextView>(R.id.userNameTv)
        val userEmailTv =
            dialog.findViewById<TextView>(R.id.userEmailTv)

        userNameTv.text = userName
        userEmailTv.text = email
        context?.let {
            Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).centerCrop().into(dialogProfileIv)
        }

        val window: Window? = dialog.window
        val wlp: WindowManager.LayoutParams? = window?.attributes

        wlp?.gravity = Gravity.TOP
        window?.attributes = wlp

        genericAdapterForClassified = ClassifiedGenericAdapter()
        genericAdapterForEvent = EventGenericAdapter()
        adsGenericAdapter1 = AdsGenericAdapter()
        adsGenericAdapter2 = AdsGenericAdapter()

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
        advertiseAdapter = AdvertiseAdapter {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToAdvertisementDetailsFragment(
                    it.advertisementId
                )
            findNavController().navigate(action)
        }

        /** This adapter is used for showing job on home screen  **/
        jobAdapter = JobSeekerAdapter()

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
                            true
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
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 0F)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        //homeScreenBinding?.availableServiceHorizontalRv?.adapter = homeEventAdapter
                        binding?.availableServiceHorizontalRv?.adapter =
                            genericAdapterForEvent
                    }
                    "Jobs" -> {
                        binding?.availableServiceHorizontalClassifiedRv?.visibility =
                            View.GONE
                        binding?.availableServiceHorizontalRv?.visibility = View.VISIBLE
                        binding?.availableServiceHorizontalRv?.margin(0F, 0f, 0F, 0F)
                        binding?.availableServiceHorizontalRv?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding?.availableServiceHorizontalRv?.adapter = jobAdapter
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
                    .skipMemoryCache(true).centerCrop().into(profilePicIv)
            }

            seeAllClassified.setOnClickListener {
                navigateToTheSpecificScreen(userInterestedService?.get(0))
            }

            seeAllEvents.setOnClickListener {
                navigateToTheSpecificScreen(navigationFromHorizontalSeeAll)
            }

            profilePicCv.setOnClickListener {
                dialog.show()
            }

            /*val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToUpdateProfileFragment()
            findNavController().navigate(action)*/

            editProfileBtn.setOnClickListener {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToUpdateProfileFragment()
                findNavController().navigate(action)
                dialog.dismiss()
            }

            logOutBtn.setOnClickListener {
                dialog.dismiss()
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Logout?")
                builder.setPositiveButton("OK") { dialog, which ->

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
                dialog.dismiss()
            }

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
                layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adsAbovePopularSectionRv.layoutManager = layoutManager2


                adsBelowFirstSectionRv.adapter = adsGenericAdapter1
                layoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adsBelowFirstSectionRv.layoutManager = layoutManager1


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
                    if (list?.contains("22") == true) {
                        list.remove("22")
                    }
                    if (list?.contains("27") == true) {
                        list.remove("27")
                    }
                    if (list?.contains("17") == true) {
                        list.remove("17")
                    }

                    if (list != null) {
                        if (list.size == 1) {
                            if (!list.contains("2")) {
                                list.add("2")
                            }
                        }
                        if (list.size == 1) {
                            if (!list.contains("8")) {
                                list.add("8")
                            }
                        }
                    }

                    callApiAccordingToInterest(list?.get(0))
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
                binding?.profilePicCv?.isEnabled = false
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
                        popularClassifiedAdapter?.setData(response.data)
                    }
                    binding?.homeConstraintLayout?.visibility = View.VISIBLE
                    binding?.popularItemsRv?.adapter = popularClassifiedAdapter
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.homeConstraintLayout?.visibility = View.GONE
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        classifiedViewModel.classifiedByUserData.observe(viewLifecycleOwner) { response ->
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
                    genericAdapterForClassified?.items = homeClassifiedWithAdList

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
            runAutoScrollBanner1()
        }

        homeViewModel.adsAbovePopularItem.observe(viewLifecycleOwner) {
            adsGenericAdapter2?.items = it
            runAutoScrollBanner2()

        }


        /*advertiseViewModel.allAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        if (response.data.size >= 4) {
                            advertiseAdapter?.setData(response.data.subList(0, 4))
                        } else {
                            advertiseAdapter?.setData(response.data)
                        }
                    }
                }
                is Resource.Error -> {
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
        }*/

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

        return binding?.root
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
                        interestAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" })
                        if (interests.isNullOrEmpty()) {
                            /**This will show all active interested services in guest user**/
                            homeInterestsServiceAdapter?.setData(response.data.filter { it.active && it.interestDesc.isNotEmpty() && it.interestDesc != "string" && it.interestDesc != "Advertise With Us" && it.interestDesc != "Shop With Us" } as MutableList<InterestResponseItem>)
                        } else {
                            //Toast.makeText(context, "${activeServiceList.size}", Toast.LENGTH_SHORT).show()
                            homeInterestsServiceAdapter?.setData(activeServiceList)
                        }
                    }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }
    }

    private fun navigateToTheSpecificScreen(interests: String?) {
        if (interests?.isNotEmpty() == true) {
            if (interests == "27" || interests == "Advertise With Us") {
                //Advertise With Us
                dashboardCommonViewModel.setIsAdvertiseClicked(true)
            } else if (interests == "2" || interests == "Classifieds") {
                //Classifieds
                dashboardCommonViewModel.setIsSeeAllClassifiedClicked(true)
            } else if (interests == "8" || interests == "Events") {
                //Events
                findNavController().navigate(R.id.action_homeScreenFragment_to_eventScreenFragment)
            } else if (interests == "3" || interests == "Immigration") {
                //Immigration
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToImmigrationScreenFragment()
                findNavController().navigate(action)
            } else if (interests == "17" || interests == "Jobs") {
                //Jobs
                /*val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToJobScreenFragment()
                findNavController().navigate(action)*/
            } else if (interests == "22" || interests == "Shop With Us") {
                //Shop With Us
                dashboardCommonViewModel.setIsShopWithUsClicked(true)
            } else if (interests == "4" || interests == "Astrology") {
                //Astrology

            } else if (interests == "26" || interests == "Business Needs") {
                //Business Needs

            } else if (interests == "10" || interests == "Community Connect") {
                //Community Connect

            } else if (interests == "13" || interests == "Foundation & Donations") {
                //Foundation & Donations

            } else if (interests == "25" || interests == "Home Needs") {
                //Home Needs

            } else if (interests == "18" || interests == "Legal Services") {
                //Legal Services

            } else if (interests == "19" || interests == "Matrimony & Weddings") {
                //Matrimony & Weddings

            } else if (interests == "20" || interests == "Medical Care") {
                //Medical Care

            } else if (interests == "21" || interests == "Real Estate") {
                //Real Estate

            } else if (interests == "5" || interests == "Sports") {
                //Sports

            } else if (interests == "16" || interests == "Student Services") {
                //Student Services

            } else if (interests == "24" || interests == "Travel and Stay") {
                //Travel and Stay

            }
        }
    }

    private fun callApiAccordingToInterest(
        interests: String? = "",
    ) {
        if (interests?.isNotEmpty() == true) {
            if (interests == "27") {
                //Advertise With Us
                priorityService = "Advertise With Us"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = advertiseAdapter

            } else if (interests == "2") {
                //Classifieds
                priorityService = "Classifieds"
                setHomeClassifiedData()
            } else if (interests == "8") {
                //Events
                priorityService = "Events"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                //homeScreenBinding?.priorityServiceRv?.adapter = homeEventAdapter
                binding?.priorityServiceRv?.adapter = genericAdapterForEvent

            } else if (interests == "3") {
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
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = immigrationAdapter
            } else if (interests == "17") {
                //Jobs
                priorityService = "Jobs"
                binding?.priorityServiceRv?.margin(left = 10f, right = 10f)
                binding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)
                binding?.priorityServiceRv?.adapter = jobAdapter
            } else if (interests == "22") {
                //Shop With Us
                /*priorityService = "Shop With Us"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
                binding?.priorityServiceRv?.visibility = View.GONE*/
            } else if (interests == "4") {
                //Astrology
                priorityService = "Astrology"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "26") {
                //Business Needs
                priorityService = "Business Needs"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "10") {
                //Community Connect
                priorityService = "Community Connect"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "13") {
                //Foundation & Donations
                priorityService = "Foundation & Donations"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "25") {
                //Home Needs
                priorityService = "Home Needs"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "18") {
                //Legal Services
                priorityService = "Legal Services"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "19") {
                //Matrimony & Weddings
                priorityService = "Matrimony & Weddings"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "20") {
                //Medical Care
                priorityService = "Medical Care"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests == "21") {
                //Real Estate
                priorityService = "Real Estate"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)

            } else if (interests == "5") {
                //Sports
                priorityService = "Sports"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)

            } else if (interests == "16") {
                //Student Services
                priorityService = "Student Services"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager = LinearLayoutManager(context)

            } else if (interests == "24") {
                //Travel and Stay
                priorityService = "Travel and Stay"
                binding?.priorityServiceRv?.margin(left = 20f, right = 20f)
                binding?.priorityServiceRv?.layoutManager =
                    LinearLayoutManager(context)
            } else if (interests.isBlank()) {
                //Toast.makeText(context, "blank", Toast.LENGTH_SHORT).show()
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


    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream = context?.assets?.open("informationcenter.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            val charset: Charset = Charsets.UTF_8
            inputStream?.read(buffer)
            inputStream?.close()
            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
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

        if (timer1 == null && timerTask1 == null&& adsGenericAdapter1?.items?.size!! >=3) {
            timer1 = Timer()
            timerTask1 = object : TimerTask() {

                override fun run() {

                    if (adRvposition1 == Int.MAX_VALUE) {
                        adRvposition1 = Int.MAX_VALUE / 2
                        binding?.adsBelowFirstSectionRv?.scrollToPosition(adRvposition1)

                    } else {
                        adRvposition1 += 2
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
        if (timer2 == null && timerTask2 == null&&adsGenericAdapter2?.items?.size!! >=3) {
            timer2 = Timer()
            timerTask2 = object : TimerTask() {

                override fun run() {

                    if (adRvposition2 == Int.MAX_VALUE) {
                        adRvposition2 = Int.MAX_VALUE / 2
                        binding?.adsAbovePopularSectionRv?.scrollToPosition(adRvposition2)

                    } else {
                        adRvposition2 += 2
                        binding?.adsAbovePopularSectionRv?.smoothScrollToPosition(adRvposition2)
                    }
                }
            }
            timer2!!.schedule(timerTask2, 4000, 4000)
        }



    }
}

