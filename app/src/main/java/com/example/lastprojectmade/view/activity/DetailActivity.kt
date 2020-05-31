package com.example.lastprojectmade.view.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.bumptech.glide.Glide
import com.example.lastprojectmade.R
import com.example.lastprojectmade.api.ApiClient
import com.example.lastprojectmade.api.EndPoint
import com.example.lastprojectmade.model.data.User
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.LINK
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.lastprojectmade.util.helper.DBHelper
import com.example.lastprojectmade.util.helper.MappingHelper
import com.example.lastprojectmade.view.fragment.FollowersFragment
import com.example.lastprojectmade.view.fragment.FollowingFragment
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var uriWithId: Uri
    private var isEdit = false

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        collapsing_layout?.title = null

        val dataIntent = intent.getParcelableExtra(EXTRA_DATA) as User?
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataIntent?.id)

        if (dataIntent != null) isEdit = true
        if (dataIntent == null) User()

        if (isEdit) {
            val cursor = contentResolver?.query(
                uriWithId,
                null,
                null,
                null,
                null
            )
            if (cursor != null) {
                MappingHelper.mapCursorToObj(cursor)
                cursor.close()
            }
        }

        val gson = GsonBuilder().create()
        val retro = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiClient.GITHUB_BASE_URL)
            .build()

        val endPoint = retro.create(EndPoint::class.java)
        endPoint.getUserDetail(dataIntent?.login.toString())
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

        fab_favs.setOnClickListener(this)
        this.setBottomNav()
    }

    @SuppressLint("Recycle")
    override fun onClick(v: View) {
        val dataIntent = intent.getParcelableExtra(EXTRA_DATA) as User
        val db: SQLiteDatabase = DBHelper(this).readableDatabase
        val sql = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE _id = '${dataIntent.id}' AND login = '${dataIntent.login}'",
            null,
            null
        )
        val contentValues = ContentValues()
        contentValues.put(USERNAME, dataIntent.login)
        contentValues.put(LINK, dataIntent.htmlURL)

        if (v.id == R.id.fab_favs) {
            if (sql.count == 0) {
                contentResolver.insert(CONTENT_URI, contentValues)
                Toast.makeText(this, "ADDED ${dataIntent.login}!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, FavoriteActivity::class.java))
            } else {
                Toast.makeText(this, "ALREADY ADDED ${dataIntent.login}!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val dataIntent = intent.getParcelableExtra(EXTRA_DATA) as User
        val db: SQLiteDatabase = DBHelper(this).readableDatabase
        val sql = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE _id = '${dataIntent.id}' AND login = '${dataIntent.login}'",
            null,
            null
        )
        if (sql.count == 1) menuInflater.inflate(R.menu.menu_delete, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Favorite"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    contentResolver.delete(uriWithId, null, null)
                    Toast.makeText(this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getData(user: User?) {
        Glide.with(applicationContext)
            .load(user?.avatarURL)
            .into(iv_avatar)

        tv_usernameDetail.text = user?.login
        tv_fullnameDetail.text = user?.name
        tv_githubLinkDetail.text = user?.htmlURL
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

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val ALERT_DIALOG_DELETE = 20
        const val ALERT_DIALOG_CLOSE = 10
    }
}