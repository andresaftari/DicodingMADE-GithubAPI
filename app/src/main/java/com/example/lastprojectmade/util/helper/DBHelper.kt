package com.example.lastprojectmade.util.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns
import com.example.lastprojectmade.util.db.DatabaseContract.FavColumns.Companion.TABLE_NAME

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbnoteapp"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${FavColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavColumns.USERNAME} TEXT," +
                " ${FavColumns.LINK} TEXT," +
                " ${FavColumns.AVATAR} TEXT," +
                " ${FavColumns.NAME} TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}