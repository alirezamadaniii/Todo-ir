package com.example.todoir.peresentaion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.databinding.ItemNumberBinding

class HourseAdapter : RecyclerView.Adapter<HourseAdapter.MyViewHolder>() {


    private val callback = object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNumberBinding
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



    inner class  MyViewHolder(val binding: ItemNumberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int){
            binding.numberCounter. text = item.toString()
//            binding.btnTryAgain.setOnClickListener {
//                onItemClick?.let {
//                    it(item)
//                }
//            }
        }
    }
//    private var onItemClick :((His)->Unit)?=null
//
//    fun setOnItemClick(listener:(His)->Unit){
//        onItemClick = listener
//    }

}