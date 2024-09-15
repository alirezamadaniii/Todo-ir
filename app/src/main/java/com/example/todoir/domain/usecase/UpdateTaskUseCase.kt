package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Task
import com.example.todoir.domain.repository.TodoRepository

class UpdateTaskUseCase(private val repository: TodoRepository) {
    suspend fun execute(task: Task) = repository.updateTask(task)
}