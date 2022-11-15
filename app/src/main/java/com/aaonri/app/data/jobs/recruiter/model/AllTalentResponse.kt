package com.aaonri.app.data.jobs.recruiter.model

data class AllTalentResponse(
    val availabilities: List<Availability>,
    val experiences: List<Experience>,
    val jobProfiles: List<JobProfile>,
    val locations: List<LocationX>,
    val visaStatus: List<VisaStatu>
)