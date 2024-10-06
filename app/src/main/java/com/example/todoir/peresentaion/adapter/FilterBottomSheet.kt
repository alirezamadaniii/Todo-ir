package com.example.todoir.peresentaion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.R
import com.example.todoir.data.model.Filter
import com.example.todoir.data.model.Language
import com.google.android.material.textview.MaterialTextView

class FilterBottomSheet (
) : RecyclerView.Adapter<FilterBottomSheet.ViewHolder>() {
    private val mList: MutableList<Filter>  = ArrayList()

    init {
        val filter1 = Filter(1,"Done")
        val filter2 = Filter(2,"Undone")
        val filter3 = Filter(3,"Date")
        val filter4 = Filter(4,"Priority")
        val filter5 = Filter(5,"Category")
        mList.add(filter1)
        mList.add(filter2)
        mList.add(filter3)
        mList.add(filter4)
        mList.add(filter5)
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