package com.example.testing.controller

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.bumptech.glide.Glide
import com.example.testing.R
import com.example.testing.api.ApiClient
import com.example.testing.api.EndPoint
import com.example.testing.fragment.FollowersFragment
import com.example.testing.fragment.FollowingFragment
import com.example.testing.model.User
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.title = null

        val dataIntent = intent.getParcelableExtra(EXTRA_DATA) as User

        val gson = GsonBuilder().create()
        val retro = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiClient.GITHUB_BASE_URL)
            .build()

        val endPoint = retro.create(EndPoint::class.java)
        endPoint.getUserDetail(dataIntent.login.toString())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    getData(user)
                },
                { error ->
                    Log.d("TAG", error.message!!)
                }
            )

        bottom_nav.setOnTabSelectedListener { position, _ ->
            when (position) {
                0 -> {
                    val followersFragment = FollowersFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_follow, followersFragment)
                        .commit()
                }
                1 -> {
                    val followingFragment = FollowingFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_follow, followingFragment)
                        .commit()
                }
            }
            return@setOnTabSelectedListener true
        }
        this.setBottomNav()
    }

    private fun setBottomNav() {
        val followersFragment = AHBottomNavigationItem(
            R.string.followers,
            R.drawable.round_add_circle_black_48dp,
            R.color.tab_1
        )
        val followingFragment = AHBottomNavigationItem(
            R.string.following,
            R.drawable.round_add_circle_outline_black_48dp,
            R.color.tab_2
        )

        bottom_nav.addItem(followersFragment)
        bottom_nav.addItem(followingFragment)

        bottom_nav.defaultBackgroundColor = Color.parseColor("#FEFEFE")
        bottom_nav.titleState = AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE_FORCE

        bottom_nav.accentColor = Color.parseColor("#F63D2B")
        bottom_nav.inactiveColor = Color.parseColor("#747474")
        bottom_nav.isColored = true
        bottom_nav.currentItem = 0
    }

    private fun getData(user: User?) {
        Glide.with(applicationContext)
            .load(user?.avatarURL)
            .into(iv_avatar)

        tv_usernameDetail.text = user?.login
        tv_fullnameDetail.text = user?.name
        tv_githubLinkDetail.text = user?.htmlURL
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
