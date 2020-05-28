package com.example.lastprojectmade.util.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.TABLE_NAME

class FavHelper(context: Context) {
    private val dbHelper = DBHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavHelper? = null

        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavHelper(context)
            }
    }

    @Throws(android.database.SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} DESC",
            null
        )
    }

    fun queryByID(id: String): Cursor =
        database.query(
            DATABASE_TABLE,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )

    fun insert(values: ContentValues?): Long =
        database.insert(DATABASE_TABLE, null, values)

    fun deleteByID(id: String): Int =
        database.delete(
            TABLE_NAME,
            "${BaseColumns._ID} = '$id'",
            null
        )
}

