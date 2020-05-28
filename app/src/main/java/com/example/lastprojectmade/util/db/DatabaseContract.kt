package com.example.lastprojectmade.util.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.example.lastprojectmade"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "login"
            const val LINK = "html_url"
            const val AVATAR = "avatar_url"
            const val NAME = "name"

            // Create URI content://com.example.lastprojectmade/favorite
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}