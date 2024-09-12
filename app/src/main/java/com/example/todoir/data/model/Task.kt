package com.example.todoir.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId:Int,
    val title:String,
    val categoryId:Int,
    val description:String,
    val time:String,
    val date:String,
    val categoryName:String,
    val categoryColor:String,
    val categoryIcon:Int,
    val flag:Int,
    val confirmTime:String,
    val confirmDate:String,
    val isCompleted:Boolean
)