package io.limkhashing.seekmax.network

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import io.limkhashing.seekmax.core.manager.SessionManager
import io.limkhashing.seekmax.helper.HttpHeaderHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestHeaderInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) {
        val jwtSessionInterceptor: HttpInterceptor
            get() = object : HttpInterceptor {
                override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
                    val jwtSession = sessionManager.getJwtSession()
                    return chain.proceed(
                        request.newBuilder()
                            .addHeader(HttpHeaderHelper.AUTHORIZATION_HEADER_PROPERTY, "Bearer $jwtSession")
                            .build()
                    )
                }
            }
}