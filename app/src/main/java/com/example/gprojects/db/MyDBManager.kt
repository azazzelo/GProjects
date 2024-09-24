package com.example.gprojects.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class MyDBManager(context: Context) {
    val MyDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = MyDbHelper.writableDatabase
    }

    fun insertToDb(title: String, description: String) {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_DESCRIPTION, description)
        }

        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "= $id"

        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }

    @SuppressLint("Range")
    fun readDbData() : ArrayList<ListItem> {
        val dataList = ArrayList<ListItem>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null,
            null, null, null, null)


            while(cursor?.moveToNext()!!) {
                val dataTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
                val dataDescription = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DESCRIPTION))
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                var item = ListItem()
                item.id = dataId
                item.title = dataTitle
                item.description = dataDescription
                dataList.add(item)
            }
        cursor.close()
        return dataList
    }

    fun closeDb() {
        MyDbHelper.close()
    }

    fun updateItem(title: String, description: String, id: Int) {
        val selection = BaseColumns._ID + "= $id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_DESCRIPTION, description)
        }

        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null )
    }

}