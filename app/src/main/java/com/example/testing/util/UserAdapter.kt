package com.example.testing.util

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testing.R
import com.example.testing.`interface`.RecyclerViewClickListener
import com.example.testing.controller.DetailActivity
import com.example.testing.fragment.FollowersFragment
import com.example.testing.model.User
import kotlinx.android.synthetic.main.item_list.view.*

class UserAdapter(private val list: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listener: RecyclerViewClickListener? = null

    class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(data: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(data.avatarURL)
                    .apply(RequestOptions().override(80, 80))
                    .into(civ_cover)

                tv_username?.text = data.login
                tv_link?.text = data.htmlURL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_list, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            val users = list[position]

            val bundle = Bundle()
            bundle.putString("username", users.login)
            FollowersFragment().arguments = bundle

            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_DATA, users)

            listener?.onRecyclerViewItemClicked(it, users)
            holder.itemView.context.startActivity(moveToDetail)
        }
    }
}