package com.example.testing.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("login")
    @Expose
    var login: String?,

    @SerializedName("name")
    @Expose
    var name: String?,

    @SerializedName("company")
    @Expose
    var company: String?,

    @SerializedName("location")
    @Expose
    var location: String?,

    @SerializedName("avatar_url")
    @Expose
    var avatarURL: String?,
    var url: String?,

    @SerializedName("html_url")
    @Expose
    var htmlURL: String?,

    @SerializedName("followers_url")
    @Expose
    var followersURL: String?,

    @SerializedName("following_url")
    @Expose
    var followingURL: String?,

    @SerializedName("repos_url")
    @Expose
    var reposURL: String?
) : Parcelable