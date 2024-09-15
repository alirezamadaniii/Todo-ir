package com.example.todoir.data.repository.datasource

import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Task
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addTask(task: Task)
    suspend fun getTask():Flow<List<Task>>
    suspend fun addCategory(category: Category)

    fun getCategory():Flow<List<Category>>
    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}