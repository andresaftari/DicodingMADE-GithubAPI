package com.example.testing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserList {
    @SerializedName("items")
    @Expose
    private var items: List<User>? = null

    fun getItems(): List<User>? = items
}