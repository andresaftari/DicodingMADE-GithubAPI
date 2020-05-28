package com.example.consumerapp.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.data.User
import com.example.consumerapp.util.adapter.FavoriteAdapter
import com.example.consumerapp.util.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.consumerapp.util.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: FavoriteAdapter

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, mAdapter.listNotes)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer App"
        mAdapter = FavoriteAdapter(this)
        rv_favs?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        if (savedInstanceState == null) loadAsync()
        else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) mAdapter.listNotes = list
        }
    }

    fun loadAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferred = async(Dispatchers.IO) {
                val cursor = contentResolver.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferred.await()
            progressbar.visibility = View.INVISIBLE

            if (user.size > 0) mAdapter.listNotes = user
            else {
                mAdapter.listNotes = ArrayList()
                Snackbar.make(rv_favs, "No favorite data :(", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
