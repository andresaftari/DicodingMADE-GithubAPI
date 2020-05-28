package com.example.lastprojectmade.util.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.lastprojectmade.util.db.DatabaseContract
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.lastprojectmade.util.helper.FavHelper

class FavProvider : ContentProvider() {
    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favHelper: FavHelper

        init {
            sUriMatcher.addURI(DatabaseContract.AUTHORITY, TABLE_NAME, FAVORITE)

            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                "$TABLE_NAME/#",
                FAVORITE_ID
            )
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?

        when (sUriMatcher.match(uri)) {
            FAVORITE -> cursor = favHelper.queryAll()
            FAVORITE_ID -> cursor = favHelper.queryByID(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        // Not used
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteByID(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }
}
