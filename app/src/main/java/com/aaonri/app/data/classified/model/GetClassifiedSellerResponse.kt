package com.aaonri.app.data.classified.model

import com.aaonri.app.data.authentication.register.model.community.Community


data class GetClassifiedSellerResponse(
    val activeUser: Boolean,
    val address1: String,
    val address2: String,
    val aliasName: String,
    val authorized: Boolean,
    val city: String,
    val community: List<Community>,
    val companyEmail: String,
    val createdOn: String,
    val deletedOn: Any,
    val emailId: String,
    val firstName: String,
    val interests: String,
    val isAdmin: Int,
    val isFullNameAsAliasName: Boolean,
    val isJobRecruiter: Boolean,
    val isPrimeUser: Boolean,
    val isSurveyCompleted: Boolean,
    val lastName: String,
    val lastUpdated: String,
    val newsletter: Boolean,
    val originCity: String,
    val originCountry: String,
    val originState: String,
    val passExpireDt: Any,
    val password: String,
    val phoneNo: String,
    val profilePic: Any,
    val regdEmailSent: Boolean,
    val registeredBy: String,
    val state: Any,
    val userFlags: List<UserFlag>,
    val userId: Int,
    val userInterests: List<Any>,
    val userName: String,
    val userType: Any,
    val zipcode: String
)