package com.example.lastprojectmade.util.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lastprojectmade.R
import com.example.lastprojectmade.model.data.User
import com.example.lastprojectmade.view.activity.DetailActivity
import kotlinx.android.synthetic.main.favorite_list.view.*
import java.util.ArrayList

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>() {

    var listNotes = ArrayList<User>()
        set(listNotes) {
            this.listNotes.clear()
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                github_fav_login?.text = user.login
                github_fav_link?.text = user.htmlURL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder =
        FavViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.favorite_list, parent, false)
        )


    override fun getItemCount(): Int = this.listNotes.size

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(listNotes[position])

        val users = listNotes[position]
        holder.itemView.setOnClickListener {
            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_DATA, users)

            holder.itemView.context.startActivity(moveToDetail)
        }
    }
}