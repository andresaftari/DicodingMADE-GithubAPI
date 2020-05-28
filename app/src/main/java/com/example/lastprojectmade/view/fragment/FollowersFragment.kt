package com.example.lastprojectmade.view.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lastprojectmade.R
import com.example.lastprojectmade.api.ApiClient
import com.example.lastprojectmade.api.EndPoint
import com.example.lastprojectmade.model.data.User
import com.example.lastprojectmade.util.`interface`.RecyclerViewClickListener
import com.example.lastprojectmade.util.adapter.FollowersAdapter
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_followers.*
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class FollowersFragment : Fragment(), RecyclerViewClickListener {
    private lateinit var mAdapter: FollowersAdapter
    private var list: List<User> = ArrayList()
    private lateinit var user: User
    private var tempList: MutableList<List<User>> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_followers, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = FollowersAdapter(list)
        if (!isNetworkAvailable())
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
        else setData()
    }

    private fun setData() {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiClient.GITHUB_BASE_URL)
            .build()

        val endPoint = retrofit.create(EndPoint::class.java)
        val tag = arguments?.getString("USERNAME", user.login)

        val caller: Call<List<User>> = endPoint.getfollowers(tag.toString())

        caller.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                try {
                    val list = response.body()!!
                    mAdapter = FollowersAdapter(list)

                    rv_list?.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = mAdapter

                        tempList.add(list)
                        if (tempList.isNotEmpty()) pb_loading3.visibility = View.GONE
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        activity,
                        "Request Failed! ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("TAG", "setData: ${e.message} --- ${e.printStackTrace()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "No internet connection ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("TAG", "onFailure: ${t.message} --- ${t.printStackTrace()}")
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager = activity
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectionManager
            .getNetworkCapabilities(connectionManager.activeNetwork)
            ?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) ?: false
    }

    override fun onRecyclerViewItemClicked(view: View, user: User) {
        Log.d("TAG", "${user.login} clicked!")
    }
}
