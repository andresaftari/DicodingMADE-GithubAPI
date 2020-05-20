package com.example.testing.util

import com.example.testing.model.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("total_count")
    @Expose
    var totalCount: Int,
    var items: MutableList<User>
)
