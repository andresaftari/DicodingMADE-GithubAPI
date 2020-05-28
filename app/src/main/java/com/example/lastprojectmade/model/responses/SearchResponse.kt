package com.example.lastprojectmade.model.responses

import com.example.lastprojectmade.model.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("total_count")
    @Expose
    var totalCount: Int,
    var items: MutableList<User>
)