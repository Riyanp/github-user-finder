package com.tiket.github.userfinder.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Config {

    private const val ENDPOINT = "https://api.github.com/"
    private const val CONNECTION_TIMEOUT = 30
    private const val READ_TIMEOUT = 30
    private const val WRITE_TIMEOUT = 30

    /**
     *
     * This method is used when calling http request.
     *
     * This method has added a timeout for connectTimeout, readTimeout and writeTimeout,
     * you can setup the all timeout at above variables.
     *
     * This method also has configured for debugging, but for security purpose
     * the debugging just added for BuildConfig.DEBUG mode.
     *
     * For parser / converter is using GSON Converter.
     *
     */
    fun getAPI(): API {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val build = OkHttpClient.Builder()
        build.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        build.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        build.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        build.addInterceptor(logging)
            .build()

        build.build()
        val client = build.build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(API::class.java)
    }
}