package com.aaonri.app.data.authentication.register.model

data class UpdateProfileRequest(
    val activeUser: Boolean? = null,
    val address1: String? = null,
    val address2: String? = null,
    val aliasName: String? = null,
    val authorized: Boolean? = null,
    val city: String? = null,
    val community: List<CommunityAuth>? = null,
    val companyEmail: String? = null,
    val emailId: String? = null,
    val firstName: String? = null,
    val interests: String? = null,
    val isAdmin: Int? = null,
    val isFullNameAsAliasName: Boolean? = null,
    val isJobRecruiter: Boolean? = null,
    val isPrimeUser: Boolean? = null,
    val isSurveyCompleted: Boolean? = null,
    val lastName: String? = null,
    val newsletter: Boolean? = null,
    val originCity: String? = null,
    val originCountry: String? = null,
    val originState: String? = null,
    val password: String? = null,
    val phoneNo: String? = null,
    val regdEmailSent: Boolean? = null,
    val registeredBy: String? = null,
    val state: Any? = null,
    val userName: String? = null,
    val userType: Any? = null,
    val zipcode: String? = null,
    val country: String? = null,
)