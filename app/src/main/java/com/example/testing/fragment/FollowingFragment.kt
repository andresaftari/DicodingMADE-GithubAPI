package com.example.testing.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.`interface`.RecyclerViewClickListener
import com.example.testing.api.ApiClient
import com.example.testing.api.EndPoint
import com.example.testing.model.User
import com.example.testing.model.UserList
import com.example.testing.util.FollowingAdapter
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_following.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class FollowingFragment : Fragment(), RecyclerViewClickListener {
    private lateinit var mAdapter: FollowingAdapter
    private var list: List<User> = ArrayList()
    private lateinit var user: User
    private var tempList: MutableList<List<User>> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_following, container, false)

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = FollowingAdapter(list)

        if (!isNetworkAvailable()) {
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
        } else {
            setData()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectionManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun setData() {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiClient.GITHUB_BASE_URL)
            .build()

        val endPoint = retrofit.create(EndPoint::class.java)
        val tag = arguments?.getString("username", user.login)

        val caller: Call<List<User>> = endPoint.getfollowing(tag.toString())

        caller.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                try {
                    val list = response.body()!!
                    mAdapter = FollowingAdapter(list)

                    rv_follow?.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = mAdapter

                        tempList.add(list)
                        if (tempList.isNotEmpty()) pb_loading2.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, "Request failed!", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "setData: ${e.message} --- ${e.printStackTrace()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "onFailure: ${t.message} --- ${t.printStackTrace()}")
            }
        })
    }

    override fun onRecyclerViewItemClicked(view: View, user: User) {
        Log.d("TAG", "${user.login} clicked!")
    }
}
