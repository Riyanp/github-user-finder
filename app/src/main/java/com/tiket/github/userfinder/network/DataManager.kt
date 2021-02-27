package com.tiket.github.userfinder.network

import android.content.Context
import com.tiket.github.userfinder.R
import com.tiket.github.userfinder.models.GithubBaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataManager private constructor(private val context: Context) {

    fun  network(tCall: Call<GithubBaseResponse>, callback: NetworkCallback) {
        val call: Call<GithubBaseResponse> = tCall
        call.enqueue(object : Callback<GithubBaseResponse> {
            override fun onResponse(
                    call: Call<GithubBaseResponse>,
                    response: Response<GithubBaseResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    when (response.code()) {
                        403 -> {
                            callback.onError(
                                    context.getString(R.string.rate_limit_reached)
                            )
                        }
                        else -> {
                            callback.onError(
                                    context.getString(R.string.error_message, response.code())
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GithubBaseResponse>, t: Throwable) {
                callback.onError(t.message)
            }
        })
    }

    companion object : SingletonHolder<DataManager, Context>(::DataManager)
}