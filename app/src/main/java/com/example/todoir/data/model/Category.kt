package com.example.todoir.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "category_table")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val icon:Int,
    val color:String
):Parcelable
