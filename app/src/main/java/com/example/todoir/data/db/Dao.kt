package com.example.todoir.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY taskId DESC")
    fun getTask(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE date =:date")
    fun getTaskAfterFilter(date:String): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE flag =:priority")
    fun getPriorityTask(priority:Int): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE categoryName =:category")
    fun getCategoryTask(category:String): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE isCompleted =:isCompleted")
    fun getCompletedTask(isCompleted:Boolean): Flow<List<Task>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun getCategory():Flow<List<Category>>

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE task_table SET isCompleted = :isCompleted WHERE taskId = :id")
    suspend fun completedTask(id:Int,isCompleted: Boolean)

}