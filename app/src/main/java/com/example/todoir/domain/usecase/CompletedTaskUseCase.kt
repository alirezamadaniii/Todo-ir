package com.example.todoir.domain.usecase

import com.example.todoir.domain.repository.TodoRepository

class CompletedTaskUseCase(private val repository: TodoRepository) {
    suspend fun execute(id:Int,isCompleted: Boolean) = repository.completedTask(id, isCompleted)
}