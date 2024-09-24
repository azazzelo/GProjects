package com.example.gprojects.db

import android.provider.BaseColumns

object MyDbNameClass {
    const val TABLE_NAME = "projects"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DESCRIPTION = "description"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "GPDb.db"

    const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT," +
            " $COLUMN_NAME_DESCRIPTION TEXT)"

    const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}