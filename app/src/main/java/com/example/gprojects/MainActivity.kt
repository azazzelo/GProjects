package com.example.gprojects

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gprojects.databinding.ActivityMainBinding
import com.example.gprojects.db.MyAdapter
import com.example.gprojects.db.MyDBManager


class MainActivity : AppCompatActivity() {
    val myDBManager = MyDBManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)


    open lateinit var bng: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bng = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bng.root)
        init()
    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()
    }

    fun onClickAdd(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun init() {
        bng.recyclerView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(bng.recyclerView)
        bng.recyclerView.adapter = myAdapter
    }

    fun fillAdapter() {
        myAdapter.updateAdapter(myDBManager.readDbData())
    }

    private fun getSwapMg() : ItemTouchHelper {
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDBManager)
            }
        })
    }

}