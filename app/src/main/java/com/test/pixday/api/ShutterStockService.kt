package com.test.pixday.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A [ShutterStockService] interface
 * Its responsible for creating a [Retrofit] client
 * to make a GET, PUT, POST and DELETE request
 * and even for the [ShutterStockService] instance methods
 */
interface ShutterStockService {

    @GET("v2/images/search")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): PhotoResponse

    companion object {
        private const val BASE_URL = "https://api.shutterstock.com/"

        fun create(accessToken: String): ShutterStockService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(OAuthInterceptor(accessToken))
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ShutterStockService::class.java)
        }
    }
}