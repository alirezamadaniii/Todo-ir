package com.example.todoir.data.repository

import com.example.todoir.data.db.CategoryCount
import com.example.todoir.data.model.Category
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

    override suspend fun getTaskAfterFilter(date: String): Flow<List<Task>> {
        return localDataSource.getTaskAfterFilter(date)
    }

    override suspend fun getPriorityTask(priority: Int): Flow<List<Task>> {
        return localDataSource.getPriorityTask(priority)
    }

    override suspend fun getCategoryTask(category: String): Flow<List<Task>> {
        return localDataSource.getCategoryTask(category)
    }

    override suspend fun getCompletedTask(isCompleted: Boolean): Flow<List<Task>> {
        return localDataSource.getCompletedTask(isCompleted)
    }

    override suspend fun addCategory(category: Category) {
        localDataSource.addCategory(category)
    }

    override fun getCategory(): Flow<List<Category>> {
        return localDataSource.getCategory()
    }

    override suspend fun deleteTask(task: Task) {
        localDataSource.deleteTask(task)
    }

    override suspend fun updateTask(task: Task) {
        localDataSource.updateTask(task)
    }

    override suspend fun completedTask(id: Int, isCompleted: Boolean) {
        localDataSource.completedTask(id, isCompleted)
    }

    override fun getCategoryChart(): Flow<List<CategoryCount>> {
        return localDataSource.getCategoryChart()
    }
}