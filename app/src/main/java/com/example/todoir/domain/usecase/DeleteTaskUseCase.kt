package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Task
import com.example.todoir.domain.repository.TodoRepository

class DeleteTaskUseCase(private val repository: TodoRepository) {
     suspend fun execute(task: Task) = repository.deleteTask(task)
}