package com.example.todoir.peresentaion.adapter

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.model.Category
import com.example.todoir.data.utils.DayInfo
import com.example.todoir.databinding.ItemCalendarBinding
import com.example.todoir.databinding.ItemCategoryBinding
import saman.zamani.persiandate.PersianDate
import java.time.LocalDate

class WeekAdapter : RecyclerView.Adapter<WeekAdapter.MyViewHolder>() {

    private var isSelected = false
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
                    cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#4345F0"))
                }

                if (isSelected){
                    cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#121212"))
                }

                root.setOnClickListener {

//                    cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#121212"))
                        cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#4345F0"))
                    isSelected = true
                    onItemClick?.let {
                        it(item.date.toString().substring(8))
                    }
                }
            }


        }


    }

    private var onItemClick: ((String) -> Unit)? = null


    fun setOnItemClick(listener: (String) -> Unit) {
        onItemClick = listener
    }
}