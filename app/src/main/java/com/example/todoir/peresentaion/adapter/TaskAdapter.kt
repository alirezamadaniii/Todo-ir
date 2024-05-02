package com.example.todoir.peresentaion.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.R
import com.example.todoir.data.model.Priority
import com.example.todoir.data.model.Task
import com.example.todoir.databinding.ItemPriorityBinding
import com.example.todoir.databinding.ItemTaskBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskAdapter @Inject constructor() : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {
    private lateinit var context: Context

    private val callback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }


        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class MyViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.tvTitleTask.text = item.title
            binding.tvTaskTime.text = item.date
            binding.btnTaskCategory.text = item.categoryName
            binding.btnTaskFlag.text = item.flag.toString()
            binding.btnTaskCategory.setBackgroundColor(Color.parseColor(item.categoryColor))
            binding.btnTaskCategory.icon = ContextCompat.getDrawable(context,item.categoryIcon)
            binding.btnTaskCategory.setIconTintResource(R.color.black)

//            binding.root.setOnClickListener {
//                onItemClick?.let {
//                    it(item)
//                }
//            }
        }
    }

    private var onItemClick: ((Priority) -> Unit)? = null

    fun setOnItemClick(listener: (Priority) -> Unit) {
        onItemClick = listener
    }


}