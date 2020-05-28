package com.example.lastprojectmade.util.adapter

import android.content.Intent
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lastprojectmade.R
import com.example.lastprojectmade.model.data.User
import com.example.lastprojectmade.view.activity.DetailActivity
import kotlinx.android.synthetic.main.search_list.view.*
import java.util.*

class SearchAdapter(private val list: MutableList<User>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {
    private lateinit var fullSearchResult: MutableList<User>
    private lateinit var filteredSearchResult: MutableList<User>

    fun update(newList: MutableList<User>) {
        this.list.clear()
        this.list.addAll(newList)
    }

    fun clear() {
        list.size
        list.clear()
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(data.avatarURL)
                    .apply(RequestOptions().override(80, 80))
                    .into(github_profile_pic)

                github_login?.text = data.login
                github_link?.text = data.htmlURL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.search_list, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(list[position])

        val users = list[position]
        holder.itemView.setOnClickListener {
            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_DATA, users)

            holder.itemView.context.startActivity(moveToDetail)
        }
    }

    override fun getFilter(): Filter = searchFilter

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<User> = ArrayList()

            if (constraint.isEmpty()) filteredList.addAll(fullSearchResult)
            else {
                val filterPattern = constraint
                    .toString()
                    .toLowerCase(Locale.ROOT)
                    .trim { it <= ' ' }

                for (item in fullSearchResult)
                    if (item.login?.toLowerCase(Locale.ROOT)!!.contains(filterPattern))
                        filteredList.add(item)

            }
            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filteredSearchResult.clear()
            filteredSearchResult.addAll(results.values as MutableList<User>)
            notifyDataSetChanged()
        }
    }
}