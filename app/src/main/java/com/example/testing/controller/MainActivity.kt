package com.example.testing.controller

import android.content.*
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.`interface`.RecyclerViewClickListener
import com.example.testing.api.ApiClient
import com.example.testing.model.*
import com.example.testing.util.UserAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), RecyclerViewClickListener {
    private lateinit var mAdapter: UserAdapter
    private var tempList: MutableList<UserList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.setHasFixedSize(true)
        rv_main.layoutManager = LinearLayoutManager(applicationContext)

        if (!isNetworkAvailable()) {
            val snack = Snackbar
                .make(coordinator_layout, "No internet connection", Snackbar.LENGTH_LONG)
                .setAction("RETRY") {
                    fetchUserData()
                }
            snack.show()
        } else {
            fetchUserData()
        }

        fab_toSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectionManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun prepareData(userList: UserList?) {
        mAdapter = UserAdapter(userList?.getItems()!!)
        rv_main?.adapter = mAdapter
    }

    private fun fetchUserData() {
        val searchParams = "language:kotlin location:indonesia"
        val apiService = ApiClient().getService()

        val userListCalls: Call<UserList> = apiService.getUserList(searchParams)
        userListCalls.enqueue(object : Callback<UserList> {

            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    prepareData(userList)

                    tempList.add(userList!!)
                    if (tempList.isNotEmpty()) pb_loading1.visibility = View.GONE
                } else Toast.makeText(
                    this@MainActivity,
                    "Request not successful",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Request failed! Internet connection required!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onRecyclerViewItemClicked(view: View, user: User) {
        user.login
    }
}
