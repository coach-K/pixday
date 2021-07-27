package com.test.pixday.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * This is [OAuthInterceptor] class
 * Its sole responsibility is to inject an
 * [accessToken] into an ongoing request.
 */
class OAuthInterceptor(private val accessToken: String) : Interceptor {

    //Intercepts the current request and adds an accessToken to the chain.
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}