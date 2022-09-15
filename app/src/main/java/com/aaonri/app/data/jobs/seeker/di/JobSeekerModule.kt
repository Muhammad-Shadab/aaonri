package com.aaonri.app.data.jobs.seeker.di

import com.aaonri.app.data.jobs.seeker.api.JobSeekerApi
import com.aaonri.app.data.jobs.seeker.api.UploadResumeApi
import com.aaonri.app.data.jobs.seeker.repository.JobSeekerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object JobSeekerModule {

    @Provides
    @ViewModelScoped
    fun provideJobSeekerApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): JobSeekerApi =
        retrofit.build().create(JobSeekerApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideJobSeekerUploadApi(
        @Named("RetrofitForScalerConverter") retrofit: Retrofit.Builder
    ): UploadResumeApi =
        retrofit.build().create(UploadResumeApi::class.java)


    @Provides
    @ViewModelScoped
    fun provideJobSeekerRepository(
        jobSeekerApi: JobSeekerApi,
        uploadResumeApi: UploadResumeApi
    ) = JobSeekerRepository(jobSeekerApi, uploadResumeApi)

}