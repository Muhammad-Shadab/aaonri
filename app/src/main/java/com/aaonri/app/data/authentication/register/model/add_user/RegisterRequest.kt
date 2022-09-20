package com.aaonri.app.data.authentication.register.model.add_user

import com.aaonri.app.data.authentication.register.model.CommunityAuth

data class RegisterRequest(
    val activeUser: Boolean,
    val address1: String,
    val address2: String,
    val aliasName: String,
    val authorized: Boolean,
    val city: String,
    val community: List<CommunityAuth>,
    val companyEmail: String,
    val emailId: String,
    val firstName: String,
    val interests: String,
    val isAdmin: Int,
    val isFullNameAsAliasName: Boolean,
    val isJobRecruiter: Boolean,
    val isPrimeUser: Boolean,
    val isSurveyCompleted: Boolean,
    val lastName: String,
    val newsletter: Boolean,
    val originCity: String,
    val originCountry: String,
    val originState: String,
    val password: String,
    val phoneNo: String,
    val picture: String,
    val regdEmailSent: Boolean,
    val registeredBy: String,
    val userName: String,
    val zipcode: String
)