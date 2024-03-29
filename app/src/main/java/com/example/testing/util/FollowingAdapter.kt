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
import com.example.testing.fragment.FollowingFragment
import com.example.testing.model.User
import kotlinx.android.synthetic.main.following_list.view.*

class FollowingAdapter(private val list: List<User>) :
    RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private val followingFragment = FollowingFragment()

    inner class FollowingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.following_list, parent, false)
        ) {

        fun bind(following: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(following.avatarURL)
                    .apply(RequestOptions().override(80, 80))
                    .into(following_pic)

                following_login?.text = following.login
                following_link?.text = following.htmlURL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder =
        FollowingViewHolder(LayoutInflater.from(parent.context), parent)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(list[position])

        val users = list[position]
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", users.login.toString())
            followingFragment.arguments = bundle

            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_DATA, users)

            holder.itemView.context.startActivity(moveToDetail)
        }
    }
}