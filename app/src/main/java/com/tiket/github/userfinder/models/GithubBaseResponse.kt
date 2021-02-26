package com.tiket.github.userfinder.models

import android.content.ClipData.Item
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GithubBaseResponse {
    @SerializedName("total_count")
    var totalCount: Int? = null

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null

    @SerializedName("items")
    var items: MutableList<UserResponse>? = null
}
