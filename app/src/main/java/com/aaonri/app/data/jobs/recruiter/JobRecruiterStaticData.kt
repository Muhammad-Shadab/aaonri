package com.aaonri.app.data.jobs.recruiter

import com.aaonri.app.data.jobs.recruiter.model.JobDetails
import com.aaonri.app.data.jobs.recruiter.model.JobProfile

object JobRecruiterStaticData {

    private var jobDetails: JobDetails? = null

    fun setJobDetailsData(value: JobDetails) {
        jobDetails = value
    }

    fun getJobDetailsValue() = jobDetails



}