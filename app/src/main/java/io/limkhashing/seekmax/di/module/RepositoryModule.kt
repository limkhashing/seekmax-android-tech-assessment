package io.limkhashing.seekmax.di.module

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.limkhashing.seekmax.core.manager.SessionManager
import io.limkhashing.seekmax.data.repository.JobsRepositoryImpl
import io.limkhashing.seekmax.domain.repository.job.JobsRepository
import io.limkhashing.seekmax.domain.repository.login.SessionRepository
import io.limkhashing.seekmax.data.repository.SessionRepositoryImpl
import io.limkhashing.seekmax.domain.repository.profile.ProfileRepository
import io.limkhashing.seekmax.data.repository.ProfileRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSeekMaxClient(apolloClient: ApolloClient): JobsRepository {
        return JobsRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        apolloClient: ApolloClient,
        sessionManager: SessionManager
    ): SessionRepository {
        return SessionRepositoryImpl(apolloClient, sessionManager)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        apolloClient: ApolloClient
    ): ProfileRepository {
        return ProfileRepositoryImpl(apolloClient)
    }
}