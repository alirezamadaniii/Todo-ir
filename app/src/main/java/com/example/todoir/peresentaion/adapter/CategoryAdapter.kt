package com.example.todoir.peresentaion.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.R
import com.example.todoir.data.model.Category
import com.example.todoir.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding
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



    inner class  MyViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category){
            binding.tvCategoryName.text = item.name
            Log.i("TAG", "bindqqqqq: "+item.icon.toString())
            binding.imgCategoryIcon.setImageResource(item.icon)
            binding.cvGrocery.setCardBackgroundColor(Color.parseColor(item.color))
            binding.root.setOnClickListener {
                onItemClick?.let {
                    it(item)
                }
            }

        }


    }

    private var onItemClick :((Category)->Unit)?=null

    fun setOnItemClick(listener:(Category)->Unit){
        onItemClick = listener
    }


}