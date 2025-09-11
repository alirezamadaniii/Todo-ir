package com.example.todoir.peresentaion.adapter

import android.annotation.SuppressLint
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
import saman.zamani.persiandate.PersianDate
import java.time.LocalDate
import kotlin.math.log

class WeekPersianAdapter : RecyclerView.Adapter<WeekPersianAdapter.MyViewHolder>() {

    private var mExpandedPosition = -1

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class MyViewHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: DayInfo) {
            val currentDate: LocalDate = LocalDate.now()
            binding.apply {

                val date=PersianDate().gregorian_to_jalali(currentDate.year,currentDate.month.value,item.date.toString().substring(8).toInt())
                val persianDate = PersianDate()
                val today = persianDate.shDay

                val isExpanded = position == mExpandedPosition
//                tvSymbolToday.text = date.get(3).toString()
                tvNumberToday.text = date.get(2).toString()
                cvBackWeekDay.setCardBackgroundColor(if (isExpanded) Color.parseColor("#FF9680") else Color.parseColor("#121212"))

                if (date[2]==today){
                    cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#4345F0"))

                }

                root.setOnClickListener{
                    onItemClick?.let {
                        it(date)
                    }
                    mExpandedPosition = if (isExpanded) -1 else position
                    notifyDataSetChanged()
                }
            }


        }


    }

    private var onItemClick: ((IntArray) -> Unit)? = null

    fun setOnItemClick(listener: (IntArray) -> Unit) {
        onItemClick = listener
    }
}