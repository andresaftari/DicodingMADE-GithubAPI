package com.example.lastprojectmade.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    @Expose
    var id: Int? = 0,

    @SerializedName("login")
    @Expose
    var login: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("avatar_url")
    @Expose
    var avatarURL: String? = null,
    var url: String? = null,

    @SerializedName("html_url")
    @Expose
    var htmlURL: String? = null
): Parcelable