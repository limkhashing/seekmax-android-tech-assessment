package io.limkhashing.seekmax.di.module

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.limkhashing.seekmax.network.RequestHeaderInterceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideApolloClient(
        okHttpClient: OkHttpClient,
        requestHeaderInterceptor: RequestHeaderInterceptor
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("http://192.168.50.247:3002") // TODO Replace with your server URL, or your localhost IP address
            .httpEngine(DefaultHttpEngine(okHttpClient))
            .addHttpInterceptor(requestHeaderInterceptor.jwtSessionInterceptor)
            .build()
    }
}