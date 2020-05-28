package com.example.consumerapp.util.helper

import android.database.Cursor
import com.example.consumerapp.data.User
import com.example.consumerapp.util.db.DatabaseContract.FavColumns.Companion.AVATAR
import com.example.consumerapp.util.db.DatabaseContract.FavColumns.Companion.LINK
import com.example.consumerapp.util.db.DatabaseContract.FavColumns.Companion.NAME
import com.example.consumerapp.util.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.consumerapp.util.db.DatabaseContract.FavColumns.Companion._ID

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val list = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val gitlink = getString(getColumnIndexOrThrow(LINK))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val name = getString(getColumnIndexOrThrow(NAME))

                list.add(User(id, username, name, avatar, null, gitlink))
            }
        }
        return list
    }

    fun mapCursorToObj(cursor: Cursor?): User {
        var userData = User()

        cursor?.apply {
            moveToFirst()
            if (cursor.moveToFirst()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val gitlink = getString(getColumnIndexOrThrow(LINK))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val name = getString(getColumnIndexOrThrow(NAME))
                userData = User(id, username, name, avatar, null, gitlink)
            }
        }
        return userData
    }
}