package com.aaonri.app.ui.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLoginBinding
import com.aaonri.app.ui.authentication.register.RegistrationActivity
import com.aaonri.app.utils.*
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    var binding: FragmentLoginBinding? = null
    val registrationViewModel: RegistrationViewModel by viewModels()
    var isEmailValid = false
    var isPasswordValid = false

    var callbackManager: CallbackManager? = null

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.gmail_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        binding?.apply {

            forgotPassTv.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_resetPassword)
            }

            guestUserLogin.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("guest", true)
                startActivity(intent)
            }

            loginBtn.setOnClickListener {

                val userEmail = loginEmailEt.text.toString().replace(" ", "").trim()
                val loginPasswordEt = loginPasswordEt.text?.trim()

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (userEmail.isNotEmpty() && loginPasswordEt?.toString()
                        ?.trim()?.isNotEmpty() == true
                    && isEmailValid && isPasswordValid
                ) {
                    registrationViewModel.loginUser(
                        Login(
                            changePass = true,
                            emailId = userEmail,
                            isAdmin = 0,
                            massage = "",
                            password = loginPasswordEt.toString(),
                            userName = ""
                        )
                    )
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter valid details", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            createAccountTv.setOnClickListener {
                val intent = Intent(requireContext(), RegistrationActivity::class.java)
                startActivity(intent)
            }

            gmailLogin.setOnClickListener { view: View? ->
                signInGoogle()
            }

            fbLoginBtn.setOnClickListener {
                facebooklogin.performClick()
            }
            facebooklogin.setReadPermissions(listOf("email"))
            facebooklogin.setFragment(this@LoginFragment)
            facebooklogin.setOnClickListener {
                //mGoogleSignInClient.signOut()
                signInFacebook()
//                LoginManager.getInstance().logInWithReadPermissions(this@LoginFragment,Arrays.asList("public_profile"))
            }
        }

        binding?.loginEmailEt?.addTextChangedListener { editable ->
            editable?.let {
                if (editable.toString().replace(" ", "").trim()
                        .isNotEmpty() && editable.toString().length > 8
                ) {
                    if (Validator.emailValidation(editable.toString().trim())) {
                        isEmailValid = true
                        //introBinding?.emailValidationTv?.visibility = View.GONE
                    } else {
                        isEmailValid = false
                        //introBinding?.emailValidationTv?.visibility = View.VISIBLE
                        binding?.emailValidationTv?.text =
                            "Please enter valid email"
                    }
                } else {
                    isEmailValid = false
                    //introBinding?.emailValidationTv?.visibility = View.GONE
                }
            }
        }

        binding?.loginPasswordEt?.addTextChangedListener { editable ->
            editable?.let {
                if (it.toString().isNotEmpty() && it.toString().length >= 8) {
                    if (Validator.passwordValidation(it.toString())) {
                        isPasswordValid = true
                        //introBinding?.passwordValidationTv?.visibility = View.GONE
                    } else {
                        isPasswordValid = false
                        binding?.passwordValidationTv?.text =
                            "Please enter valid password"
                        //introBinding?.passwordValidationTv?.visibility = View.VISIBLE
                    }
                } else {
                    isPasswordValid = false
                    //introBinding?.passwordValidationTv?.visibility = View.GONE
                }
            }
        }

        registrationViewModel.loginData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBarCommunityBottom?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBarCommunityBottom?.visibility = View.GONE
                    if (response.data?.userName.equals("failed")) {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Please enter valid email and password", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        /*response.data?.user?.interests?.let {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_INTERESTED_SERVICES, it)
                        }

                        response.data?.user?.profilePic?.let {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(
                                    Constant.USER_PROFILE_PIC,
                                    "${BuildConfig.BASE_URL}/api/v1/common/profileFile/$it"
                                )
                        }

                        response.data?.emailId?.let {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_EMAIL, it)
                        }

                        response.data?.user?.isJobRecruiter?.let {
                            context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                                ?.set(Constant.IS_JOB_RECRUITER, it)
                        }

                        if (response.data?.user?.city?.isNotEmpty() == true) {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_CITY, response.data.user.city)
                        }

                        if (response.data?.user?.zipcode?.isNotEmpty() == true) {
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_ZIP_CODE, response.data.user.zipcode)
                        }*/

                        if (response.data?.user != null) {
                            if (response.data.user.userFlags[0].flagStatus) {

                                context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                                    ?.set(Constant.IS_USER_LOGIN, true)

                                response.data.user.profilePic.let {
                                    context?.let { it1 -> PreferenceManager<String>(it1) }
                                        ?.set(
                                            Constant.USER_PROFILE_PIC,
                                            "${BuildConfig.BASE_URL}/api/v1/common/profileFile/$it"
                                        )
                                }

                                response.data.user.emailId.let {
                                    context?.let { it1 -> PreferenceManager<String>(it1) }
                                        ?.set(Constant.USER_EMAIL, it)
                                }

                                response.data.user.firstName.let {
                                    context?.let { it1 -> PreferenceManager<String>(it1) }
                                        ?.set(
                                            Constant.USER_NAME,
                                            "$it ${response.data.user.lastName}"
                                        )
                                }
                                response.data.user.city.let {
                                    context?.let { it1 -> PreferenceManager<String>(it1) }
                                        ?.set(Constant.USER_CITY, it)
                                }

                                response.data.user.interests.let {
                                    context?.let { it1 -> PreferenceManager<String>(it1) }
                                        ?.set(Constant.USER_INTERESTED_SERVICES, it)
                                }

                                val intent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            } else {
                                context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                                    ?.set(Constant.IS_USER_LOGIN, false)

                                activity?.let { it1 ->
                                    Snackbar.make(
                                        it1.findViewById(android.R.id.content),
                                        "Email address is not verified yet", Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                                ?.set(Constant.IS_USER_LOGIN, false)

                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "${response.data?.massage}", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    binding?.progressBarCommunityBottom?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })

        /* try {
             val info: PackageInfo? = activity?.packageManager?.getPackageInfo(
                 "com.aaonri.app",
                 PackageManager.GET_SIGNATURES
             )
             for (signature in info?.signatures!!) {
                 val md = MessageDigest.getInstance("SHA")
                 md.update(signature.toByteArray())
                 Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
             }
         } catch (e: PackageManager.NameNotFoundException) {
         } catch (e: NoSuchAlgorithmException) {
         }*/

        /* try {
             val info: PackageInfo? = getPackageInfo(
                 requireContext(),
                 "com.aaonri.app.dev"
             )
             for (signature in info?.signatures!!) {
                 val md: MessageDigest = MessageDigest.getInstance("SHA")
                 md.update(signature.toByteArray())
                 Log.i("KeyHash:", encodeToString(md.digest(), DEFAULT))
             }
         } catch (e: PackageManager.NameNotFoundException) {
         } catch (e: NoSuchAlgorithmException) {
         }*/

        registrationViewModel.emailAlreadyRegisterData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBarCommunityBottom?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBarCommunityBottom?.visibility = View.GONE
                    if (response.data?.status == "true") {
                        context?.let {
                            PreferenceManager<String>(
                                it
                            )[Constant.USER_EMAIL, ""]
                        }?.let { registrationViewModel.findByEmail(it) }

                        /*val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()*/
                    } else {
                        FirebaseAuth.getInstance().signOut()
                        mGoogleSignInClient.signOut()
                        LoginManager.getInstance().logOut()

                        val intent = Intent(requireContext(), RegistrationActivity::class.java)
                        intent.putExtra("newUserRegister", true)
                        startActivity(intent)
                    }
                }
                is Resource.Error -> {
                    binding?.progressBarCommunityBottom?.visibility = View.GONE
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

                    if (response.data?.userFlags?.isNotEmpty() == true) {
                        if (response.data.userFlags[0].flagStatus) {

                            context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                                ?.set(Constant.IS_USER_LOGIN, true)

                            response.data.profilePic.let {
                                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()

                                context?.let { it1 -> PreferenceManager<String>(it1) }
                                    ?.set(
                                        Constant.USER_PROFILE_PIC,
                                        "${BuildConfig.BASE_URL}/api/v1/common/profileFile/$it"
                                    )
                            }

                            response.data.emailId.let {
                                context?.let { it1 -> PreferenceManager<String>(it1) }
                                    ?.set(Constant.USER_EMAIL, it)
                            }

                            response.data.firstName.let {
                                context?.let { it1 -> PreferenceManager<String>(it1) }
                                    ?.set(
                                        Constant.USER_NAME,
                                        "$it ${response.data.lastName}"
                                    )
                            }
                            response.data.city.let {
                                context?.let { it1 -> PreferenceManager<String>(it1) }
                                    ?.set(Constant.USER_CITY, it)
                            }

                            response.data.interests.let {
                                context?.let { it1 -> PreferenceManager<String>(it1) }
                                    ?.set(Constant.USER_INTERESTED_SERVICES, it)
                            }

                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } else {
                            context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                                ?.set(Constant.IS_USER_LOGIN, false)
                            FirebaseAuth.getInstance().signOut()
                            mGoogleSignInClient.signOut()
                            LoginManager.getInstance().logOut()
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_PROFILE_PIC, "")
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "Email address is not verified yet", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                            ?.set(Constant.IS_USER_LOGIN, false)
                        FirebaseAuth.getInstance().signOut()
                        mGoogleSignInClient.signOut()
                        LoginManager.getInstance().logOut()
                        context?.let { it1 -> PreferenceManager<String>(it1) }
                            ?.set(Constant.USER_PROFILE_PIC, "")
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "User not found", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }

                    /*response.data?.userFlags?.forEach {
                        if (it.flagStatus) {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } else {
                            FirebaseAuth.getInstance().signOut()
                            mGoogleSignInClient.signOut()
                            LoginManager.getInstance().logOut()
                            context?.let { it1 -> PreferenceManager<String>(it1) }
                                ?.set(Constant.USER_PROFILE_PIC, "")
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "Email address is not verified yet", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }*/
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
        }


        return binding?.root
    }

    private fun signInFacebook() {
        binding?.progressBarCommunityBottom?.visibility = View.VISIBLE
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    //Toast.makeText(context, "ufy", Toast.LENGTH_SHORT).show()
                    binding?.progressBarCommunityBottom?.visibility = View.GONE
                }

                override fun onError(error: FacebookException) {
                    FirebaseAuth.getInstance().signOut()
                    mGoogleSignInClient.signOut()
                    LoginManager.getInstance().logOut()
                    Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                    binding?.progressBarCommunityBottom?.visibility = View.GONE
                }
            })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {

        val creadiantial = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth.signInWithCredential(creadiantial).addOnFailureListener {

            binding?.progressBarCommunityBottom?.visibility = View.GONE
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            binding?.progressBarCommunityBottom?.visibility = View.GONE

            val email = it.user?.email
            val name = it.user?.displayName

            val firstName = name?.split(" ")?.toTypedArray()

            //  context?.let { it1 -> PreferenceManager<String>(it1) }?.set(Constant.USER_CITY, response.data.user.city)

            email?.let { it1 -> EmailVerifyRequest(it1) }
                ?.let { it2 -> registrationViewModel.isEmailAlreadyRegister(it2) }

            if (email != null) {
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_EMAIL, email)
            }

            firstName?.get(0)?.let { it1 ->
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.GMAIL_FIRST_NAME, it1)
            }

            firstName?.get(1)?.let { it1 ->
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.GMAIL_LAST_NAME, it1)
            }



            it.user?.photoUrl?.let { it1 ->
                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.USER_PROFILE_PIC, it1.toString())
            }


            /*val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()*/
        }

        /*   Log.d(TAG, "handleFacebookAccessToken:$accessToken")

           val credential = accessToken?.token?.let { FacebookAuthProvider.getCredential(it) }
       if (credential != null) {
           firebaseAuth.signInWithCredential(credential)
               .addOnSuccessListener {
                   Toast.makeText(context, it.user?.displayName, Toast.LENGTH_SHORT).show()
               }.addOnCompleteListener{
                   Toast.makeText(context, it.result.user.toString(), Toast.LENGTH_SHORT).show()
               }.addOnFailureListener{
                   Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
               }
       }*/

    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {

        account.givenName?.let {
            context?.let { it1 -> PreferenceManager<String>(it1) }
                ?.set(Constant.GMAIL_FIRST_NAME, it)
        }

        account.photoUrl?.let {
            context?.let { it1 -> PreferenceManager<String>(it1) }
                ?.set(Constant.USER_PROFILE_PIC, it.toString())
        }


        account.familyName?.let {
            context?.let { it1 -> PreferenceManager<String>(it1) }
                ?.set(Constant.GMAIL_LAST_NAME, it)
        }

        account.email?.let {
            context?.let { it1 -> PreferenceManager<String>(it1) }
                ?.set(Constant.USER_EMAIL, it)
        }

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                account.email?.let {
                    EmailVerifyRequest(
                        it
                    )
                }?.let { registrationViewModel.isEmailAlreadyRegister(it) }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        binding?.loginEmailEt?.setText("")
        binding?.loginPasswordEt?.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}