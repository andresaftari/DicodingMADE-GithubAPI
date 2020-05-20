package com.example.testing.controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.testing.R
import com.example.testing.model.SearchViewModel
import com.example.testing.model.User
import com.example.testing.util.SearchAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var context: Context
    private var currentPage = 0

    companion object {
        val totalPage = SearchViewModel().getTotalPage()
        var WHO = "WhoIs"
    }

    private fun showLoading(state: Boolean) {
        if (state) pb_search.visibility = View.VISIBLE
        else pb_search.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        context = applicationContext
        currentPage = 1
        setRecyclerView()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchViewModel::class.java)
    }

    fun doSearchUser(whoIs: String, pageNum: Int) {
        showLoading(true)
        viewModel.setSearchResult(whoIs, pageNum)

        viewModel.searchResult().observe(this, Observer { list: ArrayList<User> ->
            totalPage
            searchAdapter.update(list)

            rv_search.post {
                searchAdapter.notifyDataSetChanged()
            }

            if (list.size > 0) {
                tv_searchHint.visibility = View.GONE
                showLoading(false)
            }
        })
    }

    private fun setRecyclerView() {
        val userList: MutableList<User> = ArrayList()
        searchAdapter = SearchAdapter(userList)

        rv_search?.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!rv_search.canScrollVertically(1) && dy != 0)
                        if (currentPage < totalPage) {
                            ++currentPage
                            doSearchUser(WHO, currentPage)
                        }

                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                WHO = query
                currentPage = 1
                searchAdapter.clear()
                viewModel.clear()
                doSearchUser(query, currentPage)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
}
