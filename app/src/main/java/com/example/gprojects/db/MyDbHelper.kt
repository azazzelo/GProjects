package com.example.gprojects.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, MyDbNameClass.DATABASE_NAME, null, MyDbNameClass.DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(MyDbNameClass.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(MyDbNameClass.SQL_DROP_TABLE)
        onCreate(p0)
    }
}