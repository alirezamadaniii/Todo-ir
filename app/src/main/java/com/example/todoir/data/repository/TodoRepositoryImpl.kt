package com.example.todoir.data.repository

import com.example.todoir.data.model.Task
import com.example.todoir.data.repository.datasource.LocalDataSource
import com.example.todoir.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(private val localDataSource: LocalDataSource):TodoRepository {
    override suspend fun addTask(task: Task) {
        localDataSource.addTask(task)
    }

    override suspend fun getTask(): Flow<List<Task>> {
        return localDataSource.getTask()
    }
}