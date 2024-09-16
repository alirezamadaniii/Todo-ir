package com.example.todoir.data.repository.datasourceimpl

import com.example.todoir.data.db.Dao
import com.example.todoir.data.model.Category
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

    override suspend fun addCategory(category: Category) {
        dao.addCategory(category)
    }

    override fun getCategory(): Flow<List<Category>> {
        return dao.getCategory()
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task)
    }

    override suspend fun completedTask(id: Int, isCompleted: Boolean) {
        dao.completedTask(id, isCompleted)
    }
}