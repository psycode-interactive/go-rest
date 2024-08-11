package com.psycodeinteractive.gorest.feature.users.data.di

import com.psycodeinteractive.gorest.feature.users.data.repository.UserDataRepository
import com.psycodeinteractive.gorest.feature.users.data.source.remote.UserApiService
import com.psycodeinteractive.gorest.feature.users.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsUserRepository(
        userDataRepository: UserDataRepository
    ): UserRepository

    companion object {
        @Singleton
        @Provides
        fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Singleton
        @Provides
        fun providesUsersApiService(
            retrofit: Retrofit
        ): UserApiService = retrofit.create(UserApiService::class.java)
    }
}
