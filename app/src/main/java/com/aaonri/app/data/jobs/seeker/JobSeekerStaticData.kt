package com.aaonri.app.data.jobs.seeker

import com.aaonri.app.data.jobs.seeker.model.AllJobsResponse

object JobSeekerStaticData {

    private var jobList: AllJobsResponse? = null

    fun setJobListData(value: AllJobsResponse) {
        jobList = value
    }

    fun getJobListValue() = jobList


}