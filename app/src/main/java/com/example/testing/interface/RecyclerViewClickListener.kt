package com.example.testing.`interface`

import android.view.View
import com.example.testing.model.User

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, user: User)
}