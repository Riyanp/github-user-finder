package com.tiket.github.userfinder.network

import com.tiket.github.userfinder.models.GithubBaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface API {

    @GET("search/users")
    fun fetchApiSearchUser(
        @QueryMap filters: Map<String, String>
    ): Call<GithubBaseResponse>

}