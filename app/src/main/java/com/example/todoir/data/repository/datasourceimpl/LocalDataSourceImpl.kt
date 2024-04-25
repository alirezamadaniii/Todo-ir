package com.example.todoir.data.repository.datasourceimpl

import com.example.todoir.data.db.Dao
import com.example.todoir.data.model.Task
import com.example.todoir.data.repository.datasource.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val dao: Dao):LocalDataSource {
    override suspend fun addTask(task: Task) {
        dao.addTask(task)
    }

    override suspend fun getTask(): Flow<List<Task>> {
        return dao.getTask()
    }
}