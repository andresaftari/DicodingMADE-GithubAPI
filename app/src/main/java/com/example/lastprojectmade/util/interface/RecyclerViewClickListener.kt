package com.example.lastprojectmade.util.`interface`

import android.view.View
import com.example.lastprojectmade.model.data.User

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, user: User)
}