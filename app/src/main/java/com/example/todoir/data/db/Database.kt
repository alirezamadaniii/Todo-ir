package com.example.todoir.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoir.data.model.Task

@Database(
    entities = [Task::class],
    version = 2,
    exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun dao():Dao
}