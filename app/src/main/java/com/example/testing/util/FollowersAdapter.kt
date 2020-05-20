package com.example.testing.util

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testing.R
import com.example.testing.controller.DetailActivity
import com.example.testing.fragment.FollowersFragment
import com.example.testing.model.User
import kotlinx.android.synthetic.main.follower_list.view.*

class FollowersAdapter(private val list: List<User>) :
    RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    private val followerfragment = FollowersFragment()

    inner class FollowersViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.follower_list, parent, false)
        ) {

        fun bind(followers: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(followers.avatarURL)
                    .apply(RequestOptions().override(80, 80))
                    .into(follower_pic)

                follower_login?.text = followers.login
                follower_link?.text = followers.htmlURL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder =
        FollowersViewHolder(LayoutInflater.from(parent.context), parent)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(list[position])

        val users = list[position]
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", users.login.toString())
            followerfragment.arguments = bundle

            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_DATA, users)

            holder.itemView.context.startActivity(moveToDetail)
        }
    }
}