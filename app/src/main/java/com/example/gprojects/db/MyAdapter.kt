package com.example.gprojects.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gprojects.EditActivity
import com.example.gprojects.R

class MyAdapter(listMain: ArrayList<ListItem>, contextM: Context) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {

    var listArray = listMain
    var context = contextM

    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.textView)
        val context = contextV

        fun setData(item: ListItem) {
            tvTitle.text = item.title
            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_DESC_KEY, item.description)
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, parent, false), context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    fun updateAdapter(listItems: List<ListItem>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int, dbManager: MyDBManager) {
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }


}