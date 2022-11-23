package com.aaonri.app.data.jobs.recruiter

import com.aaonri.app.data.jobs.recruiter.model.JobDetails
import com.aaonri.app.data.jobs.recruiter.model.JobProfile

object JobRecruiterStaticData {

    private var jobDetails: JobDetails? = null
    private lateinit var talentList: List<JobProfile>

    fun setJobDetailsData(value: JobDetails) {
        jobDetails = value
    }

    fun getJobDetailsValue() = jobDetails

    fun setTalentListData(value: List<JobProfile>) {
        talentList = value
    }

    fun getTalentListValue() = talentList


}