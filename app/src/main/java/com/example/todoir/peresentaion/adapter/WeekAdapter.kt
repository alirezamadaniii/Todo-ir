package com.example.todoir.peresentaion.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.R
import com.example.todoir.data.utils.DayInfo
import com.example.todoir.databinding.ItemCalendarBinding
import java.time.LocalDate

class WeekAdapter : RecyclerView.Adapter<WeekAdapter.MyViewHolder>() {


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



                val isExpanded = position == mExpandedPosition
                cvBackWeekDay.setCardBackgroundColor(if (isExpanded) Color.parseColor("#FF9680") else Color.parseColor("#121212"))
                if (item.dayOfWeek==currentDate.dayOfWeek.name){
                    cvBackWeekDay.setCardBackgroundColor(Color.parseColor("#4345F0"))
                }

                root.setOnClickListener{
                    onItemClick?.let {
                        it(item.date.toString().substring(8))
                    }
                    mExpandedPosition = if (isExpanded) -1 else position
                    notifyDataSetChanged()
                }


            }

        }

    }

    private var onItemClick: ((String) -> Unit)? = null


    fun setOnItemClick(listener: (String) -> Unit) {
        onItemClick = listener
    }
}