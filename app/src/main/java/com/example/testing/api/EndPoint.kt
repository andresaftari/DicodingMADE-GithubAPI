package com.example.testing.api

import com.example.testing.model.User
import com.example.testing.model.UserList
import com.example.testing.util.SearchResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface EndPoint {
    @GET("/search/users")
    fun getUserList(@Query("q") filter: String): Call<UserList>

    @GET("/search/users")
    fun getSearchResult(
        @Query("q") whoIs: String,
        @Query("page") pageNumber: Int
    ): Call<SearchResponse>

    @GET("/users/{username}")
    fun getUserDetail(@Path("username") username: String): Observable<User>

    @GET("users/{username}/followers")
    fun getfollowers(@Path("username") username: String): Call<List<User>>

    @GET("/users/{username}/following")
    fun getfollowing(@Path("username") username: String): Call<List<User>>
}