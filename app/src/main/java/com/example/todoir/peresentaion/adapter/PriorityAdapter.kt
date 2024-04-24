package com.example.todoir.peresentaion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Priority
import com.example.todoir.databinding.ItemCategoryBinding
import com.example.todoir.databinding.ItemPriorityBinding

class PriorityAdapter: RecyclerView.Adapter<PriorityAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Priority>(){
        override fun areItemsTheSame(oldItem: Priority, newItem: Priority): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Priority, newItem: Priority): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPriorityBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    inner class  MyViewHolder(val binding: ItemPriorityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Priority){
            binding.tvCategoryName.text = item.name
            binding.root.setOnClickListener {
                onItemClick?.let {
                    it(item)
                }
            }
        }
    }

    private var onItemClick :((Priority)->Unit)?=null

    fun setOnItemClick(listener:(Priority)->Unit){
        onItemClick = listener
    }

}