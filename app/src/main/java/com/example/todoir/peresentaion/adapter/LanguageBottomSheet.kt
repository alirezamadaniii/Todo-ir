package com.example.todoir.peresentaion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.R
import com.example.todoir.data.model.Language
import com.google.android.material.textview.MaterialTextView

class LanguageBottomSheet (
) : RecyclerView.Adapter<LanguageBottomSheet.ViewHolder>() {
    private val mList: MutableList<Language>  = ArrayList()

    init {
        val language1 = Language(1,"English")
        val language2 = Language(2,"Persian")
        mList.add(language1)
        mList.add(language2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: MaterialTextView = itemView.findViewById(R.id.txt)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = mList[position]
        holder.item.text = item.name
        holder.item.setOnClickListener {
            onItemClick?.let {
                it(item.name)
            }
        }
    }

    private var onItemClick :((String)->Unit)?=null

    fun setOnItemClick(listener:(String)->Unit){
        onItemClick = listener
    }
}