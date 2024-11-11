package com.example.todoir.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId:Int,
    val title:String,
    val categoryId:Int,
    val description:String,
    var time:String,
    var date:String,
    val categoryName:String,
    var categoryColor:String,
    var categoryIcon:Int,
    var flag:Int,
    val confirmTime:String,
    val confirmDate:String,
    var isCompleted:Boolean
):Parcelable