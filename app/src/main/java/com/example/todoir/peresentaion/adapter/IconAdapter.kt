package com.example.todoir.peresentaion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.model.Icon
import com.example.todoir.data.model.Priority
import com.example.todoir.databinding.ItemIconBinding
import com.example.todoir.databinding.ItemPriorityBinding

class IconAdapter : RecyclerView.Adapter<IconAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Icon>(){
        override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemIconBinding
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



    inner class  MyViewHolder(val binding: ItemIconBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Icon){
            binding.imgIconCategory.setImageResource(item.icon)
            binding.root.setOnClickListener {
                onItemClick?.let {
                    it(item)
                }
            }
        }
    }

    private var onItemClick :((Icon)->Unit)?=null

    fun setOnItemClick(listener:(Icon)->Unit){
        onItemClick = listener
    }


}