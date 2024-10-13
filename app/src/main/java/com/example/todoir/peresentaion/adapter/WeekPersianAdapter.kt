package com.example.todoir.peresentaion.adapter

import android.annotation.SuppressLint
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
import saman.zamani.persiandate.PersianDate
import java.time.LocalDate
import kotlin.math.log

class WeekPersianAdapter : RecyclerView.Adapter<WeekPersianAdapter.MyViewHolder>() {

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

                val date=PersianDate().gregorian_to_jalali(currentDate.year,currentDate.month.value,item.date.toString().substring(8).toInt())
                val persianDate = PersianDate()
                val today = persianDate.shDay

//                tvSymbolToday.text = date.get(3).toString()
                tvNumberToday.text = date.get(2).toString()
                if (date[2]==today){
                    cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#4345F0"))
                }
            }


        }


    }

    private var onItemClick: ((Category) -> Unit)? = null

    fun setOnItemClick(listener: (Category) -> Unit) {
        onItemClick = listener
    }
}