package com.tiket.github.userfinder.network

import com.tiket.github.userfinder.models.GithubBaseResponse


interface NetworkCallback{
    fun onSuccess(model: GithubBaseResponse)
    fun onError(message: String?)
}