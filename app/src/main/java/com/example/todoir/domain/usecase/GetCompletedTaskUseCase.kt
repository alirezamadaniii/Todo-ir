package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Task
import com.example.todoir.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetCompletedTaskUseCase (private val repository: TodoRepository) {
    suspend fun execute(isCompleted:Boolean): Flow<List<Task>> {
        return repository.getCompletedTask(isCompleted)
    }
}