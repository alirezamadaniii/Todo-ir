package com.example.todoir.peresentaion.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Task
import com.example.todoir.data.utils.DayInfo
import com.example.todoir.databinding.ItemCalendarBinding
import com.example.todoir.databinding.ItemTimelineCalanderBinding
import saman.zamani.persiandate.PersianDate
import java.time.LocalDate

class TimeLineCalendarAdapter : RecyclerView.Adapter<TimeLineCalendarAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTimelineCalanderBinding
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


    inner class MyViewHolder(val binding: ItemTimelineCalanderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.apply {
                tvTitleTimeline.text = item.title
                imgIconTimeline.setImageResource(item.categoryIcon)
                imgIconTimeline.setColorFilter(Color.parseColor(item.categoryColor))
                tvTime.text = item.time
            }
        }


    }

    private var onItemClick: ((Category) -> Unit)? = null

    fun setOnItemClick(listener: (Category) -> Unit) {
        onItemClick = listener
    }
}