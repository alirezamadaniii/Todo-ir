package com.example.todoir.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Task

@Database(
    entities = [Task::class,Category::class],
    version = 9,
    exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun dao():Dao
}