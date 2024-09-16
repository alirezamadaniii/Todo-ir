package com.example.todoir.domain.repository

import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun addTask(task: Task)

    suspend fun getTask(): Flow<List<Task>>

    suspend fun addCategory(category: Category)

    fun getCategory():Flow<List<Category>>

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun completedTask(id:Int,isCompleted: Boolean)


}