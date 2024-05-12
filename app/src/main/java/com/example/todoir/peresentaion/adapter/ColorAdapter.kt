package com.example.todoir.peresentaion.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.model.CategoryColor
import com.example.todoir.databinding.ItemColorBinding

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.MyViewHolder>() {

    private var isEnabled = false
    private val callback = object : DiffUtil.ItemCallback<CategoryColor>(){
        override fun areItemsTheSame(oldItem: CategoryColor, newItem: CategoryColor): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: CategoryColor, newItem: CategoryColor): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemColorBinding
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



    inner class  MyViewHolder(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryColor){
                binding.cvColorCategory.setCardBackgroundColor(Color.parseColor(item.color))
            binding.root.setOnClickListener {
                if (isEnabled == false){
                    binding.imgDone.visibility = View.VISIBLE
                    isEnabled = true
                }else{
                    binding.imgDone.visibility = View.GONE
                    isEnabled = false
                }
                onItemClick?.let {
                    it(item)
                }
            }
        }
    }

    private var onItemClick :((CategoryColor)->Unit)?=null

    fun setOnItemClick(listener:(CategoryColor)->Unit){
        onItemClick = listener
    }


}