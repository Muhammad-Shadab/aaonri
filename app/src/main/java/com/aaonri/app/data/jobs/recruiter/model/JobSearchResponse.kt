package com.aaonri.app.data.jobs.recruiter.model

data class JobSearchResponse(
    val companies: List<Company>,
    val experienceLevels: List<ExperienceLevel>,
    val industries: List<Industry>,
    val jobDetailsList: List<JobDetails>,
    val jobTypes: List<JobType>,
    val locations: List<Location>
)