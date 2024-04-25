package com.example.todoir.data.repository.datasource

import com.example.todoir.data.model.Task
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addTask(task: Task)
    suspend fun getTask():Flow<List<Task>>
}