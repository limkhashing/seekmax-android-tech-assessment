package io.limkhashing.seekmax.di.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.limkhashing.seekmax.core.manager.SessionManager
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Singleton
    @Provides
    fun provideSessionDataStore(@Named(SessionManager.SESSION) sessionDataStore: DataStore<Preferences>): SessionManager {
        return SessionManager(sessionDataStore)
    }
}