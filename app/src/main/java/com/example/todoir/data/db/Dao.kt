package com.example.todoir.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addTask(task: Task)

    @Query("SELECT * FROM task_table")
    fun getTask(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun getCategory():Flow<List<Category>>

    @Delete
    suspend fun deleteTask(task: Task)

}