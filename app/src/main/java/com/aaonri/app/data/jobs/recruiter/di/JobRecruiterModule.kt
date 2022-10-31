package com.aaonri.app.data.jobs.recruiter.di

import com.aaonri.app.data.jobs.recruiter.api.JobRecruiterApi
import com.aaonri.app.data.jobs.recruiter.repository.JobRecruiterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object JobRecruiterModule {

    @Provides
    @ViewModelScoped
    fun provideJobRecruiterApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): JobRecruiterApi =
        retrofit.build().create(JobRecruiterApi::class.java)


    @Provides
    @ViewModelScoped
    fun provideJobRecruiterRepository(
        jobRecruiterApi: JobRecruiterApi
    ) = JobRecruiterRepository(jobRecruiterApi)


}