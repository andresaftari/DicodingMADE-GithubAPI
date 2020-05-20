package com.example.testing.model

import android.util.Log
import androidx.lifecycle.*
import com.example.testing.api.ApiClient
import com.example.testing.util.SearchResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.ceil

class SearchViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var total = 0
    private val userData: ArrayList<User> = ArrayList()

    fun setSearchResult(whoIs: String, page: Int) {
        Retrofit.Builder()
            .baseUrl(ApiClient.GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val endPoint = ApiClient().getService()
        val caller: Call<SearchResponse> = endPoint.getSearchResult(whoIs, page)

        caller.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.body() != null) {
                    total = (
                            ceil(
                                response.body()?.totalCount!!.toDouble() / 30.toDouble()
                            ).toInt())
                    userData.addAll(response.body()!!.items)
                    mutableLiveData.value = userData
                    Log.d("DBG", "OK")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("DBG", "Failed! ${t.message}")
            }
        })
    }

    fun searchResult(): LiveData<ArrayList<User>> = mutableLiveData

    fun clear() {
        userData.clear()
        mutableLiveData.value = ArrayList()
    }

    fun getTotalPage(): Int = total
}