package com.example.todoir.peresentaion.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.model.Category
import com.example.todoir.data.utils.DayInfo
import com.example.todoir.databinding.ItemCalendarBinding
import com.example.todoir.databinding.ItemCategoryBinding
import java.time.LocalDate

class WeekAdapter : RecyclerView.Adapter<WeekAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<DayInfo>() {
        override fun areItemsTheSame(oldItem: DayInfo, newItem: DayInfo): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: DayInfo, newItem: DayInfo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCalendarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class MyViewHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayInfo) {
            val currentDate: LocalDate = LocalDate.now()
            binding.apply {

                tvSymbolToday.text = item.dayOfWeek.substring(0,3)
                tvNumberToday.text = item.date.toString().substring(8)
                if (item.dayOfWeek==currentDate.dayOfWeek.name){
                    tvSymbolToday.text="aaaaaaaaa"
                }
            }


//            binding.root.setOnClickListener {
//                onItemClick?.let {
//                    it(item)
//                }
//            }

        }


    }

    private var onItemClick: ((Category) -> Unit)? = null

    fun setOnItemClick(listener: (Category) -> Unit) {
        onItemClick = listener
    }
}