package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Task
import com.example.todoir.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetPriorityTaskUseCase (private val repository: TodoRepository) {
    suspend fun execute(priority:Int): Flow<List<Task>> {
        return repository.getPriorityTask(priority)
    }
}